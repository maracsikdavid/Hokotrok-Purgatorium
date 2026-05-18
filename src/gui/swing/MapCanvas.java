package gui.swing;

import gui.layout.MapLayout;
import gui.snapshot.GameSnapshot;
import gui.swing.render.MapRenderer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A pálya vizuális kirajzolásáért felelős Swing komponens.
 * A rajzolást a snapshot, a layout és a kijelölési állapot alapján a MapRenderer felé delegálja.
 */
public class MapCanvas extends JPanel {
    private static final int FIT_MARGIN = 42;

    private GameSnapshot snapshot;
    private MapLayout layout;
    private SelectionState selectionState;
    private MapRenderer mapRenderer = new MapRenderer();
    private transient MapClickListener mapClickListener;

    /**
     * Létrehozza a rajzvásznat és beköti az egérkattintásokat.
     */
    public MapCanvas() {
        installMouseHandling();
    }

    /**
     * Beállítja a térképelem-kattintások kezelőjét.
     *
     * @param mapClickListener az új kattintáskezelő
     */
    public void setMapClickListener(MapClickListener mapClickListener) {
        this.mapClickListener = mapClickListener;
    }

    /**
     * Beállítja a rajzolandó játékállapotot.
     *
     * @param snapshot az új snapshot
     */
    public void setSnapshot(GameSnapshot snapshot) {
        this.snapshot = snapshot;
        repaint();
    }

    /**
     * Beállítja a rajzoláshoz használt layoutot.
     *
     * @param layout az új layout
     */
    public void setLayout(MapLayout layout) {
        this.layout = layout;
        repaint();
    }

    /**
     * Beállítja az aktuális kijelölési állapotot.
     *
     * @param selectionState az új kijelölési állapot
     */
    public void setSelectionState(SelectionState selectionState) {
        this.selectionState = selectionState;
        repaint();
    }

    /**
     * Beállítja a térképet kirajzoló renderelő objektumot.
     *
     * @param mapRenderer az új renderelő
     */
    public void setMapRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    /**
     * A Swing rajzolási ciklus belépési pontja.
     *
     * @param graphics a rajzolási kontextus
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (mapRenderer != null) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            try {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                applyLayoutFit(graphics2D);
                mapRenderer.render(graphics2D, snapshot, layout, selectionState);
            } finally {
                graphics2D.dispose();
            }
        }
    }

    private void applyLayoutFit(Graphics2D graphics) {
        LayoutTransform transform = calculateLayoutTransform();
        if (transform == null) {
            return;
        }

        graphics.translate(transform.offsetX, transform.offsetY);
        graphics.scale(transform.scale, transform.scale);
    }

    private void installMouseHandling() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (mapClickListener == null) {
                    return;
                }
                CanvasHit hit = findHit(event.getPoint());
                mapClickListener.onMapClick(hit.id, hit.category, SwingUtilities.isRightMouseButton(event));
            }
        });
    }

    private CanvasHit findHit(Point screenPoint) {
        Point layoutPoint = toLayoutPoint(screenPoint);
        CanvasHit vehicleHit = findVehicleHit(layoutPoint);
        if (vehicleHit.isPresent()) {
            return vehicleHit;
        }
        CanvasHit nodeHit = findNodeHit(layoutPoint);
        if (nodeHit.isPresent()) {
            return nodeHit;
        }
        return findLaneHit(layoutPoint);
    }

    private Point toLayoutPoint(Point screenPoint) {
        LayoutTransform transform = calculateLayoutTransform();
        if (transform == null || screenPoint == null) {
            return screenPoint == null ? new Point(0, 0) : new Point(screenPoint);
        }
        int x = (int) Math.round((screenPoint.x - transform.offsetX) / transform.scale);
        int y = (int) Math.round((screenPoint.y - transform.offsetY) / transform.scale);
        return new Point(x, y);
    }

    private LayoutTransform calculateLayoutTransform() {
        LayoutBounds bounds = calculateLayoutBounds();
        if (bounds == null) {
            return null;
        }

        Insets insets = getInsets();
        int availableWidth = Math.max(1, getWidth() - insets.left - insets.right);
        int availableHeight = Math.max(1, getHeight() - insets.top - insets.bottom);
        double scale = Math.min(availableWidth / bounds.getWidth(), availableHeight / bounds.getHeight());
        double offsetX = insets.left + ((availableWidth - (bounds.getWidth() * scale)) / 2.0) - (bounds.minX * scale);
        double offsetY = insets.top + ((availableHeight - (bounds.getHeight() * scale)) / 2.0) - (bounds.minY * scale);
        return new LayoutTransform(scale, offsetX, offsetY);
    }

    private CanvasHit findVehicleHit(Point point) {
        if (snapshot == null || point == null) {
            return CanvasHit.none();
        }
        CanvasHit bestHit = CanvasHit.none();
        double bestDistance = 18.0;
        for (GameSnapshot.Entry vehicle : snapshot.getEntriesByCategory("vehicle")) {
            Point vehiclePoint = resolveVehiclePoint(vehicle);
            double distance = distance(point, vehiclePoint);
            if (distance <= bestDistance) {
                bestDistance = distance;
                bestHit = new CanvasHit(vehicle.getId(), "vehicle");
            }
        }
        return bestHit;
    }

    private CanvasHit findNodeHit(Point point) {
        if (layout == null || snapshot == null || point == null) {
            return CanvasHit.none();
        }
        CanvasHit bestHit = CanvasHit.none();
        double bestDistance = 24.0;
        for (GameSnapshot.Entry node : snapshot.getEntriesByCategory("node")) {
            Point nodePoint = layout.getNodePosition(node.getId());
            double distance = distance(point, nodePoint);
            if (distance <= bestDistance) {
                bestDistance = distance;
                bestHit = new CanvasHit(node.getId(), "node");
            }
        }
        return bestHit;
    }

    private CanvasHit findLaneHit(Point point) {
        if (snapshot == null || point == null) {
            return CanvasHit.none();
        }
        CanvasHit bestHit = CanvasHit.none();
        double bestDistance = 12.0;
        for (GameSnapshot.Entry lane : snapshot.getEntriesByCategory("lane")) {
            LaneGeometry geometry = resolveLaneGeometry(lane);
            double distance = geometry == null ? Double.POSITIVE_INFINITY
                : pointToSegmentDistance(point, geometry.start, geometry.end);
            if (distance <= bestDistance) {
                bestDistance = distance;
                bestHit = new CanvasHit(lane.getId(), "lane");
            }
        }
        return bestHit;
    }

    private Point resolveVehiclePoint(GameSnapshot.Entry vehicle) {
        if (vehicle == null || snapshot == null) {
            return null;
        }
        GameSnapshot.Entry lane = snapshot.findEntryById(vehicle.getAttribute("currentLaneId"));
        LaneGeometry geometry = resolveLaneGeometry(lane);
        if (geometry == null) {
            return null;
        }
        double ratio = progressRatio(vehicle, lane);
        double x = geometry.start.x + (geometry.end.x - geometry.start.x) * ratio;
        double y = geometry.start.y + (geometry.end.y - geometry.start.y) * ratio;
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    private LaneGeometry resolveLaneGeometry(GameSnapshot.Entry lane) {
        if (lane == null || layout == null || snapshot == null) {
            return null;
        }
        String roadId = lane.getAttribute("roadId");
        if (roadId == null) {
            roadId = lane.getAttribute("road");
        }
        GameSnapshot.Entry road = snapshot.findEntryById(roadId);
        String fromId = road == null ? layout.getRoadFromNode(roadId) : road.getAttribute("from");
        String toId = road == null ? layout.getRoadToNode(roadId) : road.getAttribute("to");
        if (toId == null && road != null) {
            toId = road.getAttribute("targetNodeId");
        }
        Point start = layout.getNodePosition(fromId);
        Point end = layout.getNodePosition(toId);
        if (start == null || end == null) {
            return null;
        }
        return offsetGeometry(lane.getId(), start, end);
    }

    private LaneGeometry offsetGeometry(String laneId, Point start, Point end) {
        int offset = layout == null ? 0 : layout.getLaneOffset(laneId);
        double dx = (double) end.x - start.x;
        double dy = (double) end.y - start.y;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length <= 0.0001) {
            return new LaneGeometry(start, end);
        }
        double normalX = -dy / length;
        double normalY = dx / length;
        Point shiftedStart = new Point(
            (int) Math.round(start.x + normalX * offset),
            (int) Math.round(start.y + normalY * offset));
        Point shiftedEnd = new Point(
            (int) Math.round(end.x + normalX * offset),
            (int) Math.round(end.y + normalY * offset));
        return new LaneGeometry(shiftedStart, shiftedEnd);
    }

    private double progressRatio(GameSnapshot.Entry vehicle, GameSnapshot.Entry lane) {
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

    private double pointToSegmentDistance(Point point, Point start, Point end) {
        if (point == null || start == null || end == null) {
            return Double.POSITIVE_INFINITY;
        }
        double abx = (double) end.x - start.x;
        double aby = (double) end.y - start.y;
        double apx = (double) point.x - start.x;
        double apy = (double) point.y - start.y;
        double abLengthSquared = abx * abx + aby * aby;
        if (abLengthSquared <= 0.0001) {
            return distance(point, start);
        }
        double t = clamp((apx * abx + apy * aby) / abLengthSquared, 0.0, 1.0);
        Point closest = new Point((int) Math.round(start.x + abx * t), (int) Math.round(start.y + aby * t));
        return distance(point, closest);
    }

    private double distance(Point left, Point right) {
        if (left == null || right == null) {
            return Double.POSITIVE_INFINITY;
        }
        double dx = (double) left.x - right.x;
        double dy = (double) left.y - right.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double parseDouble(String value, double fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String trimmedValue = value.trim();
        if (!trimmedValue.matches("-?\\d+(\\.\\d+)?")) {
            return fallback;
        }
        return Double.parseDouble(trimmedValue);
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    private LayoutBounds calculateLayoutBounds() {
        if (layout == null || layout.getNodePositions().isEmpty()) {
            return null;
        }

        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Point point : layout.getNodePositions().values()) {
            if (point == null) {
                continue;
            }
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        if (!Double.isFinite(minX) || !Double.isFinite(minY) || !Double.isFinite(maxX) || !Double.isFinite(maxY)) {
            return null;
        }

        return new LayoutBounds(minX - FIT_MARGIN, minY - FIT_MARGIN, maxX + FIT_MARGIN, maxY + FIT_MARGIN);
    }

    private static class LayoutBounds {
        private final double minX;
        private final double minY;
        private final double maxX;
        private final double maxY;

        LayoutBounds(double minX, double minY, double maxX, double maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        double getWidth() {
            return Math.max(1.0, maxX - minX);
        }

        double getHeight() {
            return Math.max(1.0, maxY - minY);
        }
    }

    private static class LayoutTransform {
        private final double scale;
        private final double offsetX;
        private final double offsetY;

        LayoutTransform(double scale, double offsetX, double offsetY) {
            this.scale = scale;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }

    private static class LaneGeometry {
        private final Point start;
        private final Point end;

        LaneGeometry(Point start, Point end) {
            this.start = start;
            this.end = end;
        }
    }

    private static class CanvasHit {
        private final String id;
        private final String category;

        CanvasHit(String id, String category) {
            this.id = id;
            this.category = category;
        }

        static CanvasHit none() {
            return new CanvasHit(null, null);
        }

        boolean isPresent() {
            return id != null && !id.isBlank();
        }
    }

    /**
     * Térképelem-kattintások eseménykezelője.
     */
    public interface MapClickListener {
        void onMapClick(String elementId, String category, boolean rightClick);
    }
}