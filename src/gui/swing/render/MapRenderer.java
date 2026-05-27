package gui.swing.render;

import gui.layout.MapLayout;
import gui.layout.RoadLayoutHint;
import gui.snapshot.GameSnapshot;
import gui.swing.GameColors;
import gui.swing.SelectionState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.geom.CubicCurve2D;

/**
 * A teljes pálya kirajzolását koordináló renderelő váza.
 * A konkrét rajzolási részfeladatokat a csomópont-, sáv- és járműrenderelők kapják meg.
 */
public class MapRenderer {
    private static final Font LANE_LABEL_FONT = new Font("SansSerif", Font.BOLD, 10);
    private static final float LABEL_BORDER_WIDTH = 0.5f;
    private final NodeRenderer nodeRenderer = new NodeRenderer();
    private final LaneRenderer laneRenderer = new LaneRenderer();
    private final VehicleRenderer vehicleRenderer = new VehicleRenderer();

    /**
     * Kirajzolja a teljes térképet az univerzális snapshot és layout alapján.
     *
     * @param graphics a rajzolási kontextus
     * @param snapshot az aktuális játékállapot pillanatképe
     * @param layout a rajzolási koordinátákat tartalmazó layout
     * @param selectionState az aktuális kijelölési állapot
     */
    public void render(Graphics2D graphics, GameSnapshot snapshot, MapLayout layout, SelectionState selectionState) {
        if (graphics == null || snapshot == null || layout == null) {
            return;
        }

        java.util.Map<String, LanePath> lanePaths = new java.util.LinkedHashMap<>();
        java.util.Map<String, Integer> nodeRequiredWidths = new java.util.LinkedHashMap<>();
        RenderContext context = new RenderContext(graphics, snapshot, layout, selectionState, lanePaths,
                nodeRequiredWidths);

        renderLaneGroups(context);
        renderNodes(context);

        renderVehicles(graphics, snapshot, lanePaths, selectionState);
    }

    private void renderLaneGroups(RenderContext context) {
        java.util.Map<String, java.util.List<GameSnapshot.Entry>> lanesByRoad = groupLanesByRoad(
                context.snapshot.getEntriesByCategory("lane"));
        for (java.util.Map.Entry<String, java.util.List<GameSnapshot.Entry>> grouped : lanesByRoad.entrySet()) {
            renderLaneGroup(context, grouped.getKey(), grouped.getValue());
        }
    }

    private java.util.Map<String, java.util.List<GameSnapshot.Entry>> groupLanesByRoad(List<GameSnapshot.Entry> lanes) {
        java.util.Map<String, java.util.List<GameSnapshot.Entry>> lanesByRoad = new java.util.LinkedHashMap<>();
        for (GameSnapshot.Entry lane : lanes) {
            String roadId = resolveRoadId(lane);
            lanesByRoad.computeIfAbsent(roadId, key -> new java.util.ArrayList<>()).add(lane);
        }
        return lanesByRoad;
    }

    private String resolveRoadId(GameSnapshot.Entry lane) {
        String roadId = lane.getAttribute("roadId");
        if (roadId == null) {
            roadId = lane.getAttribute("road");
        }
        return roadId == null ? "__no_road__:" + lane.getId() : roadId;
    }

    private void renderLaneGroup(RenderContext context, String roadId, java.util.List<GameSnapshot.Entry> groupedLanes) {
        sortLanes(groupedLanes);
        RoadEndpoints endpoints = resolveRoadEndpoints(context.snapshot, context.layout, roadId, groupedLanes);
        LaneGroupGeometry geometry = createLaneGroupGeometry(context, roadId, endpoints, groupedLanes.size());
        if (geometry == null) {
            return;
        }

        for (int index = 0; index < groupedLanes.size(); index++) {
            renderLane(context, groupedLanes.get(index), geometry, index);
        }
    }

    private void sortLanes(java.util.List<GameSnapshot.Entry> groupedLanes) {
        groupedLanes.sort((leftEntry, rightEntry) -> {
            String left = leftEntry == null ? null : leftEntry.getId();
            String right = rightEntry == null ? null : rightEntry.getId();
            if (left == null && right == null) {
                return 0;
            }
            if (left == null) {
                return -1;
            }
            if (right == null) {
                return 1;
            }
            return left.compareTo(right);
        });
    }

    private RoadEndpoints resolveRoadEndpoints(GameSnapshot snapshot, MapLayout layout, String roadId,
                                               java.util.List<GameSnapshot.Entry> groupedLanes) {
        RoadEndpoints endpoints = resolveRoadEntryEndpoints(snapshot, layout, roadId);
        if ((endpoints.fromId == null || endpoints.toId == null) && !groupedLanes.isEmpty()) {
            GameSnapshot.Entry firstLane = groupedLanes.get(0);
            String fromId = endpoints.fromId == null ? firstLane.getAttribute("from") : endpoints.fromId;
            String toId = endpoints.toId == null ? firstLane.getAttribute("to") : endpoints.toId;
            return new RoadEndpoints(fromId, toId);
        }
        return endpoints;
    }

    private RoadEndpoints resolveRoadEntryEndpoints(GameSnapshot snapshot, MapLayout layout, String roadId) {
        if (roadId == null || roadId.startsWith("__no_road__:")) {
            return new RoadEndpoints(null, null);
        }

        String fromId = null;
        String toId = null;
        GameSnapshot.Entry road = snapshot.findEntryById(roadId);
        if (road != null) {
            fromId = road.getAttribute("from");
            toId = road.getAttribute("to");
            String roadTarget = road.getAttribute("targetNodeId");
            if (toId == null && roadTarget != null) {
                toId = roadTarget;
            }
        }
        if (fromId == null) {
            fromId = layout.getRoadFromNode(roadId);
        }
        if (toId == null) {
            toId = layout.getRoadToNode(roadId);
        }
        return new RoadEndpoints(fromId, toId);
    }

    private LaneGroupGeometry createLaneGroupGeometry(RenderContext context, String roadId, RoadEndpoints endpoints,
                                                      int laneCount) {
        if (endpoints == null || endpoints.fromId == null || endpoints.toId == null) {
            return null;
        }

        Point start = context.layout.getNodePosition(endpoints.fromId);
        Point end = context.layout.getNodePosition(endpoints.toId);
        if (start == null || end == null) {
            return null;
        }

        double dx = (double) end.x - start.x;
        double dy = (double) end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length <= 0.0001) {
            return null;
        }

        int laneSpacing = LaneRenderer.LANE_INNER_WIDTH;
        int laneBodyWidth = LaneRenderer.LANE_INNER_WIDTH;
        int laneOutlineExtra = Math.max(1, (LaneRenderer.LANE_OUTLINE_WIDTH - LaneRenderer.LANE_INNER_WIDTH) / 2);
        int effectiveLaneWidth = laneBodyWidth + (2 * laneOutlineExtra);
        int bundleDiameter = Math.max(NodeRenderer.BASE_NODE_DIAMETER, laneCount * laneSpacing + effectiveLaneWidth);
        int fromDiameter = Math.max(context.nodeRequiredWidths.getOrDefault(endpoints.fromId, NodeRenderer.BASE_NODE_DIAMETER),
                bundleDiameter);
        int toDiameter = Math.max(context.nodeRequiredWidths.getOrDefault(endpoints.toId, NodeRenderer.BASE_NODE_DIAMETER),
                bundleDiameter);
        context.nodeRequiredWidths.put(endpoints.fromId, fromDiameter);
        context.nodeRequiredWidths.put(endpoints.toId, toDiameter);

        double baseNodeHalfRadius = NodeRenderer.BASE_NODE_DIAMETER / 2.0;
        LaneGroupGeometry geometry = new LaneGroupGeometry();
        geometry.roadId = roadId;
        geometry.endpoints = endpoints;
        geometry.start = start;
        geometry.end = end;
        geometry.length = length;
        geometry.ux = dx / length;
        geometry.uy = dy / length;
        geometry.fromRadius = Math.max(baseNodeHalfRadius, fromDiameter / 2.0);
        geometry.toRadius = Math.max(baseNodeHalfRadius, toDiameter / 2.0);
        geometry.laneSpacing = laneSpacing;
        geometry.laneCount = laneCount;
        return geometry;
    }

    private void renderLane(RenderContext context, GameSnapshot.Entry lane, LaneGroupGeometry geometry, int index) {
        double finalOffset = resolveLaneOffset(context.layout, lane, geometry, index);
        LaneDrawPoints drawPoints = createDrawPoints(geometry, finalOffset);
        RoadLayoutHint roadHint = resolveRoadHint(context.layout, geometry.roadId);
        boolean selectedLane = isLaneSelected(lane, context.selectionState);

        LanePath lanePath;
        if (roadHint.isCurveEnabled()) {
            lanePath = renderCurveLane(context, lane, geometry, drawPoints, roadHint, selectedLane, finalOffset);
        } else {
            lanePath = renderLineLane(context.graphics, lane, drawPoints, selectedLane);
        }

        context.lanePaths.put(lane.getId(), lanePath);
        String laneLabel = createLaneLabel(context.snapshot, lane, geometry.roadId, context.layout);
        drawLaneLabel(context.graphics, laneLabel, lanePath, resolveLaneLabelColor(lane));
    }

    private double resolveLaneOffset(MapLayout layout, GameSnapshot.Entry lane, LaneGroupGeometry geometry, int index) {
        int baseOffset = layout.getLaneOffset(lane.getId());
        double autoOffset = (index - ((geometry.laneCount - 1) / 2.0)) * geometry.laneSpacing;
        return baseOffset + autoOffset;
    }

    private LaneDrawPoints createDrawPoints(LaneGroupGeometry geometry, double finalOffset) {
        double startX = geometry.start.x + geometry.nx() * finalOffset;
        double startY = geometry.start.y + geometry.ny() * finalOffset;
        double endX = geometry.end.x + geometry.nx() * finalOffset;
        double endY = geometry.end.y + geometry.ny() * finalOffset;
        double overlapIntoNode = 3.0;

        startX += geometry.ux * (geometry.fromRadius + overlapIntoNode);
        startY += geometry.uy * (geometry.fromRadius + overlapIntoNode);
        endX -= geometry.ux * (geometry.toRadius + overlapIntoNode);
        endY -= geometry.uy * (geometry.toRadius + overlapIntoNode);

        return new LaneDrawPoints(
                new Point((int) Math.round(startX), (int) Math.round(startY)),
                new Point((int) Math.round(endX), (int) Math.round(endY))
        );
    }

    private RoadLayoutHint resolveRoadHint(MapLayout layout, String roadId) {
        RoadLayoutHint roadHint = layout.getRoadHint(roadId);
        if (roadHint != null) {
            return roadHint;
        }
        return new RoadLayoutHint(roadId, "curve", 0, "auto");
    }

    private LanePath renderCurveLane(RenderContext context, GameSnapshot.Entry lane, LaneGroupGeometry geometry,
                                     LaneDrawPoints drawPoints, RoadLayoutHint roadHint, boolean selectedLane,
                                     double finalOffset) {
        CubicCurve2D.Double curve = createCurve(context, geometry, drawPoints, roadHint, finalOffset);
        LanePath lanePath = LanePath.curve(curve);
        laneRenderer.render(context.graphics, lane, curve, selectedLane);
        return lanePath;
    }

    private CubicCurve2D.Double createCurve(RenderContext context, LaneGroupGeometry geometry, LaneDrawPoints drawPoints,
                                            RoadLayoutHint roadHint, double finalOffset) {
        int curveOffset = roadHint.resolveAutoControlOffset(geometry.laneCount, geometry.length);
        double laneCurveOffset = curveOffset + (finalOffset * 0.8);
        double[] startTan = roadHint.resolveStartTangent(geometry.ux, geometry.uy);
        double[] endTan = roadHint.resolveEndTangent(geometry.ux, geometry.uy);
        double tangentLen = clamp(geometry.length * 0.35, 14.0, 64.0);

        ControlPoints controlPoints = new ControlPoints(
                drawPoints.start.x + startTan[0] * tangentLen + geometry.nx() * laneCurveOffset * 0.35,
                drawPoints.start.y + startTan[1] * tangentLen + geometry.ny() * laneCurveOffset * 0.35,
                drawPoints.end.x + endTan[0] * tangentLen + geometry.nx() * laneCurveOffset * 0.35,
                drawPoints.end.y + endTan[1] * tangentLen + geometry.ny() * laneCurveOffset * 0.35
        );

        AvoidanceTarget avoidTarget = findAvoidanceTarget(context, geometry, drawPoints);
        if (avoidTarget != null) {
            controlPoints = applyAvoidance(controlPoints, drawPoints, geometry, laneCurveOffset, avoidTarget.radius);
        }

        return new CubicCurve2D.Double(
                drawPoints.start.x, drawPoints.start.y,
                controlPoints.ctrl1X, controlPoints.ctrl1Y,
                controlPoints.ctrl2X, controlPoints.ctrl2Y,
                drawPoints.end.x, drawPoints.end.y
        );
    }

    private AvoidanceTarget findAvoidanceTarget(RenderContext context, LaneGroupGeometry geometry,
                                                LaneDrawPoints drawPoints) {
        AvoidanceTarget bestTarget = null;
        double bestDanger = Double.POSITIVE_INFINITY;
        for (GameSnapshot.Entry candidateNode : context.snapshot.getEntriesByCategory("node")) {
            AvoidanceTarget candidate = createAvoidanceCandidate(context, geometry, drawPoints, candidateNode);
            if (candidate != null && candidate.distance < bestDanger) {
                bestDanger = candidate.distance;
                bestTarget = candidate;
            }
        }
        return bestTarget;
    }

    private AvoidanceTarget createAvoidanceCandidate(RenderContext context, LaneGroupGeometry geometry,
                                                     LaneDrawPoints drawPoints,
                                                     GameSnapshot.Entry candidateNode) {
        String candidateId = candidateNode.getId();
        if (candidateId == null || candidateId.equals(geometry.endpoints.fromId)
                || candidateId.equals(geometry.endpoints.toId)) {
            return null;
        }
        Point candidatePos = context.layout.getNodePosition(candidateId);
        if (candidatePos == null) {
            return null;
        }
        int candidateDiameter = context.nodeRequiredWidths.getOrDefault(candidateId, NodeRenderer.BASE_NODE_DIAMETER);
        double candidateRadius = 0.5 * candidateDiameter + 14.0;
        double distance = pointToSegmentDistance(candidatePos.x, candidatePos.y, drawPoints.start.x, drawPoints.start.y,
                drawPoints.end.x, drawPoints.end.y);
        return distance < candidateRadius ? new AvoidanceTarget(candidateRadius, distance) : null;
    }

    private ControlPoints applyAvoidance(ControlPoints controlPoints, LaneDrawPoints drawPoints,
                                         LaneGroupGeometry geometry, double lateral, double avoidRadius) {
        double avoidPush = Math.max(avoidRadius + 10.0, Math.abs(lateral) + 18.0);
        ControlPoints left = controlPoints.shift(geometry.nx() * avoidPush, geometry.ny() * avoidPush);
        ControlPoints right = controlPoints.shift(-geometry.nx() * avoidPush, -geometry.ny() * avoidPush);
        return curveScore(drawPoints, left) <= curveScore(drawPoints, right) ? left : right;
    }

    private double curveScore(LaneDrawPoints drawPoints, ControlPoints controlPoints) {
        return dist(drawPoints.start.x, drawPoints.start.y, controlPoints.ctrl1X, controlPoints.ctrl1Y)
                + dist(controlPoints.ctrl1X, controlPoints.ctrl1Y, controlPoints.ctrl2X, controlPoints.ctrl2Y)
                + dist(controlPoints.ctrl2X, controlPoints.ctrl2Y, drawPoints.end.x, drawPoints.end.y);
    }

    private LanePath renderLineLane(Graphics2D graphics, GameSnapshot.Entry lane, LaneDrawPoints drawPoints,
                                    boolean selectedLane) {
        LanePath lanePath = LanePath.line(drawPoints.start, drawPoints.end);
        laneRenderer.render(graphics, lane, drawPoints.start, drawPoints.end, selectedLane);
        return lanePath;
    }

    private void renderNodes(RenderContext context) {
        List<GameSnapshot.Entry> nodes = context.snapshot.getEntriesByCategory("node");
        for (GameSnapshot.Entry node : nodes) {
            Point pos = context.layout.getNodePosition(node.getId());
            if (pos != null) {
                int requestedDiameter = context.nodeRequiredWidths.getOrDefault(node.getId(), NodeRenderer.BASE_NODE_DIAMETER);
                nodeRenderer.render(context.graphics, node, pos, requestedDiameter, context.layout.getNodeLabel(node.getId()),
                        isNodeSelected(node, context.selectionState));
            }
        }
    }

    /**
     * Visszaadja a csomópont-renderelőt.
     *
     * @return a node renderer
     */
    public NodeRenderer getNodeRenderer() {
        return nodeRenderer;
    }

    /**
     * Visszaadja a sáv-renderelőt.
     *
     * @return a lane renderer
     */
    public LaneRenderer getLaneRenderer() {
        return laneRenderer;
    }

    /**
     * Visszaadja a jármű-renderelőt.
     *
     * @return a vehicle renderer
     */
    public VehicleRenderer getVehicleRenderer() {
        return vehicleRenderer;
    }

    private void renderVehicles(Graphics2D graphics, GameSnapshot snapshot, java.util.Map<String, LanePath> lanePaths,
                                SelectionState selectionState) {
        List<GameSnapshot.Entry> vehicles = snapshot.getEntriesByCategory("vehicle");
        for (GameSnapshot.Entry vehicle : vehicles) {
            String laneId = vehicle.getAttribute("currentLaneId");
            LanePath lanePath = lanePaths.get(laneId);
            if (lanePath == null) {
                continue;
            }

            GameSnapshot.Entry lane = snapshot.findEntryById(laneId);
            double progressRatio = resolveProgressRatio(vehicle, lane);
            boolean selected = isVehicleSelected(vehicle, selectionState);
            if (lanePath.isLine()) {
                vehicleRenderer.render(graphics, vehicle, lanePath.getStart(), lanePath.getEnd(), selected);
            } else {
                PathPosition position = lanePath.pointAt(progressRatio);
                vehicleRenderer.render(graphics, vehicle, position.getPoint(), position.getAngleRadians(), selected);
            }
        }
    }

    private void drawLaneLabel(Graphics2D graphics, String label, LanePath lanePath, Color textColor) {
        if (label == null || label.isBlank() || lanePath == null) {
            return;
        }
        PathPosition labelPosition = lanePath.pointAt(0.5);
        Point point = labelPosition.getPoint();
        graphics.setFont(LANE_LABEL_FONT);
        float x = point.x - (graphics.getFontMetrics().stringWidth(label) / 2.0f);
        float y = point.y - 14.0f;
        drawOutlinedText(graphics, label, x, y, textColor == null ? Color.WHITE : textColor);
    }

    private void drawOutlinedText(Graphics2D graphics, String text, float x, float y, Color fillColor) {
        GlyphVector glyphVector = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), text);
        Shape textShape = glyphVector.getOutline(x, y);
        Stroke originalStroke = graphics.getStroke();
        Color originalColor = graphics.getColor();

        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(LABEL_BORDER_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.draw(textShape);
        graphics.setColor(fillColor);
        graphics.fill(textShape);

        graphics.setStroke(originalStroke);
        graphics.setColor(originalColor);
    }

    private String createLaneLabel(GameSnapshot snapshot, GameSnapshot.Entry lane, String roadId, MapLayout layout) {
        int laneNumber = resolveLaneNumber(snapshot, lane, roadId);
        String roadLabel = resolveRoadLabel(roadId, layout);
        return laneNumber + ". sáv (" + roadLabel + ")";
    }

    private int resolveLaneNumber(GameSnapshot snapshot, GameSnapshot.Entry lane, String roadId) {
        if (snapshot == null || lane == null || roadId == null) {
            return 1;
        }
        GameSnapshot.Entry roadEntry = snapshot.findEntryById(roadId);
        if (roadEntry == null) {
            return 1;
        }
        String laneIds = roadEntry.getAttribute("laneIds");
        if (laneIds == null || laneIds.isBlank()) {
            return 1;
        }
        String[] tokens = laneIds.split(",");
        for (int index = 0; index < tokens.length; index++) {
            if (lane.getId().equals(tokens[index].trim())) {
                return index + 1;
            }
        }
        return 1;
    }

    private String resolveRoadLabel(String roadId, MapLayout layout) {
        String roadLabel = layout.getRoadLabel(roadId);
        if (roadLabel == null || roadLabel.isBlank()) {
            roadLabel = normalizeRoadId(roadId);
        }
        return roadLabel;
    }

    private String normalizeRoadId(String roadId) {
        if (roadId == null || roadId.isBlank()) {
            return "út";
        }
        return roadId.replace('_', ' ');
    }

    private Color resolveLaneLabelColor(GameSnapshot.Entry lane) {
        String conditionRaw = lane == null ? null : lane.getAttribute("condition");
        if ((conditionRaw == null || conditionRaw.isBlank()) && lane != null) {
            conditionRaw = lane.getAttribute("state");
        }
        String normalizedCondition = normalizeCondition(conditionRaw);
        return GameColors.laneColor(normalizedCondition, isSaltedIce(lane));
    }

    private String normalizeCondition(String conditionRaw) {
        if (conditionRaw == null) {
            return "";
        }
        String value = conditionRaw.trim();
        if (value.isEmpty()) {
            return "";
        }
        int lastDot = value.lastIndexOf('.');
        if (lastDot >= 0 && lastDot < value.length() - 1) {
            value = value.substring(lastDot + 1);
        }
        if (value.endsWith("Condition") && value.length() > "Condition".length()) {
            value = value.substring(0, value.length() - "Condition".length());
        }
        return value.toLowerCase();
    }

    private boolean isSaltedIce(GameSnapshot.Entry lane) {
        if (lane == null) {
            return false;
        }
        String saltTimer = lane.getAttribute("saltTimer");
        return saltTimer != null && !saltTimer.isBlank() && !"-1".equals(saltTimer);
    }

    private double resolveProgressRatio(GameSnapshot.Entry vehicle, GameSnapshot.Entry lane) {
        double progress = parseDouble(vehicle.getAttribute("progress"), 0.0);
        double laneLength = parseDouble(vehicle.getAttribute("laneLength"), 0.0);
        if (laneLength <= 0.0 && lane != null) {
            laneLength = parseDouble(lane.getAttribute("length"), 0.0);
        }
        if (laneLength <= 0.0) {
            return 0.5;
        }
        return clamp(progress / laneLength, 0.0, 1.0);
    }

    private boolean isLaneSelected(GameSnapshot.Entry lane, SelectionState selectionState) {
        if (lane == null || selectionState == null) {
            return false;
        }
        return equals(lane.getId(), selectionState.getSelectedLaneId())
                || equals(lane.getAttribute("roadId"), selectionState.getSelectedRoadId());
    }

    private boolean isNodeSelected(GameSnapshot.Entry node, SelectionState selectionState) {
        return node != null && selectionState != null && equals(node.getId(), selectionState.getHoveredElementId());
    }

    private boolean isVehicleSelected(GameSnapshot.Entry vehicle, SelectionState selectionState) {
        return vehicle != null && selectionState != null && equals(vehicle.getId(), selectionState.getSelectedVehicleId());
    }

    private static double parseDouble(String value, double fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String trimmedValue = value.trim();
        if (!trimmedValue.matches("-?\\d+(\\.\\d+)?")) {
            return fallback;
        }
        return Double.parseDouble(trimmedValue);
    }

    private static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private static boolean equals(String left, String right) {
        return left == null ? right == null : left.equals(right);
    }

    private static class RoadEndpoints {
        private final String fromId;
        private final String toId;

        RoadEndpoints(String fromId, String toId) {
            this.fromId = fromId;
            this.toId = toId;
        }
    }

    private static class RenderContext {
        private final Graphics2D graphics;
        private final GameSnapshot snapshot;
        private final MapLayout layout;
        private final SelectionState selectionState;
        private final java.util.Map<String, LanePath> lanePaths;
        private final java.util.Map<String, Integer> nodeRequiredWidths;

        RenderContext(Graphics2D graphics, GameSnapshot snapshot, MapLayout layout, SelectionState selectionState,
                      java.util.Map<String, LanePath> lanePaths,
                      java.util.Map<String, Integer> nodeRequiredWidths) {
            this.graphics = graphics;
            this.snapshot = snapshot;
            this.layout = layout;
            this.selectionState = selectionState;
            this.lanePaths = lanePaths;
            this.nodeRequiredWidths = nodeRequiredWidths;
        }
    }

    private static class LaneGroupGeometry {
        private String roadId;
        private RoadEndpoints endpoints;
        private Point start;
        private Point end;
        private double length;
        private double ux;
        private double uy;
        private double fromRadius;
        private double toRadius;
        private int laneSpacing;
        private int laneCount;

        double nx() {
            return -uy;
        }

        double ny() {
            return ux;
        }
    }

    private static class LaneDrawPoints {
        private final Point start;
        private final Point end;

        LaneDrawPoints(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
    }

    private static class ControlPoints {
        private final double ctrl1X;
        private final double ctrl1Y;
        private final double ctrl2X;
        private final double ctrl2Y;

        ControlPoints(double ctrl1X, double ctrl1Y, double ctrl2X, double ctrl2Y) {
            this.ctrl1X = ctrl1X;
            this.ctrl1Y = ctrl1Y;
            this.ctrl2X = ctrl2X;
            this.ctrl2Y = ctrl2Y;
        }

        ControlPoints shift(double dx, double dy) {
            return new ControlPoints(ctrl1X + dx, ctrl1Y + dy, ctrl2X + dx, ctrl2Y + dy);
        }
    }

    private static class AvoidanceTarget {
        private final double radius;
        private final double distance;

        AvoidanceTarget(double radius, double distance) {
            this.radius = radius;
            this.distance = distance;
        }
    }

    private static class LanePath {
        private final Point start;
        private final Point end;
        private final CubicCurve2D.Double curve;

        private LanePath(Point start, Point end, CubicCurve2D.Double curve) {
            this.start = start;
            this.end = end;
            this.curve = curve;
        }

        static LanePath line(Point start, Point end) {
            return new LanePath(start, end, null);
        }

        static LanePath curve(CubicCurve2D.Double curve) {
            return new LanePath(null, null, curve);
        }

        boolean isLine() {
            return curve == null;
        }

        Point getStart() {
            return start;
        }

        Point getEnd() {
            return end;
        }

        PathPosition pointAt(double ratio) {
            double t = clamp(ratio, 0.0, 1.0);
            if (curve != null) {
                return curvePointAt(t);
            }
            double dx = (double) end.x - start.x;
            double dy = (double) end.y - start.y;
            double x = start.x + dx * t;
            double y = start.y + dy * t;
            double angle = Math.atan2(dy, dx);
            return new PathPosition(new Point((int) Math.round(x), (int) Math.round(y)), angle);
        }

        private PathPosition curvePointAt(double t) {
            double oneMinusT = 1.0 - t;
            double x = oneMinusT * oneMinusT * oneMinusT * curve.getX1()
                    + 3.0 * oneMinusT * oneMinusT * t * curve.getCtrlX1()
                    + 3.0 * oneMinusT * t * t * curve.getCtrlX2()
                    + t * t * t * curve.getX2();
            double y = oneMinusT * oneMinusT * oneMinusT * curve.getY1()
                    + 3.0 * oneMinusT * oneMinusT * t * curve.getCtrlY1()
                    + 3.0 * oneMinusT * t * t * curve.getCtrlY2()
                    + t * t * t * curve.getY2();

            double dx = 3.0 * oneMinusT * oneMinusT * (curve.getCtrlX1() - curve.getX1())
                    + 6.0 * oneMinusT * t * (curve.getCtrlX2() - curve.getCtrlX1())
                    + 3.0 * t * t * (curve.getX2() - curve.getCtrlX2());
            double dy = 3.0 * oneMinusT * oneMinusT * (curve.getCtrlY1() - curve.getY1())
                    + 6.0 * oneMinusT * t * (curve.getCtrlY2() - curve.getCtrlY1())
                    + 3.0 * t * t * (curve.getY2() - curve.getCtrlY2());
            return new PathPosition(new Point((int) Math.round(x), (int) Math.round(y)), Math.atan2(dy, dx));
        }
    }

    private static class PathPosition {
        private final Point point;
        private final double angleRadians;

        PathPosition(Point point, double angleRadians) {
            this.point = point;
            this.angleRadians = angleRadians;
        }

        Point getPoint() {
            return point;
        }

        double getAngleRadians() {
            return angleRadians;
        }
    }

    private static double pointToSegmentDistance(double px, double py, double ax, double ay, double bx, double by) {
        double abx = bx - ax;
        double aby = by - ay;
        double apx = px - ax;
        double apy = py - ay;

        double abLen2 = abx * abx + aby * aby;
        if (abLen2 <= 0.0000001) {
            return dist(px, py, ax, ay);
        }

        double t = (apx * abx + apy * aby) / abLen2;
        t = clamp(t, 0.0, 1.0);

        double cx = ax + t * abx;
        double cy = ay + t * aby;
        return dist(px, py, cx, cy);
    }

    private static double dist(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
