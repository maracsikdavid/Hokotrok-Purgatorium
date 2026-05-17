package gui.swing.render;

import gui.layout.MapLayout;
import gui.layout.RoadLayoutHint;
import gui.snapshot.GameSnapshot;
import gui.swing.SelectionState;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.Point;
import java.awt.geom.CubicCurve2D;

/**
 * A teljes pálya kirajzolását koordináló renderelő váza.
 * A konkrét rajzolási részfeladatokat a csomópont-, sáv- és járműrenderelők kapják meg.
 */
public class MapRenderer {
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
        // Sávok (Lanes) kirajzolása (hogy alul legyenek)
        List<GameSnapshot.Entry> lanes = snapshot.getEntriesByCategory("lane");

        // Először lane-ek csoportosítása road szerint, hogy több lane esetén automatikus egymás melletti kiosztás legyen
        java.util.Map<String, java.util.List<GameSnapshot.Entry>> lanesByRoad = new java.util.LinkedHashMap<>();
        for (GameSnapshot.Entry lane : lanes) {
            String roadId = lane.getAttribute("roadId");
            if (roadId == null) roadId = lane.getAttribute("road");
            if (roadId == null) roadId = "__no_road__:" + lane.getId();

            java.util.List<GameSnapshot.Entry> group = lanesByRoad.get(roadId);
            if (group == null) {
                group = new java.util.ArrayList<>();
                lanesByRoad.put(roadId, group);
            }
            group.add(lane);
        }

        java.util.Map<String, Integer> nodeRequiredWidths = new java.util.LinkedHashMap<>();
        java.util.Map<String, Integer> nodeRequiredHeights = new java.util.LinkedHashMap<>();

        for (java.util.Map.Entry<String, java.util.List<GameSnapshot.Entry>> grouped : lanesByRoad.entrySet()) {
            String roadId = grouped.getKey();
            java.util.List<GameSnapshot.Entry> groupedLanes = grouped.getValue();

            // stabil sorrend id alapján
            groupedLanes.sort((a, b) -> {
                String left = a == null ? null : a.getId();
                String right = b == null ? null : b.getId();
                if (left == null && right == null) return 0;
                if (left == null) return -1;
                if (right == null) return 1;
                return left.compareTo(right);
            });

            // végpontok road alapján (lane-ek többségénél ez az egyetlen megbízható forrás)
            String fromId = null;
            String toId = null;

            if (roadId != null && !roadId.startsWith("__no_road__:")) {
                GameSnapshot.Entry road = snapshot.findEntryById(roadId);
                if (road != null) {
                    fromId = road.getAttribute("from");
                    toId = road.getAttribute("to");
                    String roadTarget = road.getAttribute("targetNodeId");
                    if (toId == null && roadTarget != null) {
                        toId = roadTarget;
                    }
                }

                if (fromId == null) fromId = layout.getRoadFromNode(roadId);
                if (toId == null) toId = layout.getRoadToNode(roadId);
            }

            // fallback: ha nincs roados adat, próbáljuk az első lane attribútumait
            if ((fromId == null || toId == null) && !groupedLanes.isEmpty()) {
                GameSnapshot.Entry firstLane = groupedLanes.get(0);
                if (fromId == null) fromId = firstLane.getAttribute("from");
                if (toId == null) toId = firstLane.getAttribute("to");
            }

            if (fromId == null || toId == null) {
                continue;
            }

            Point start = layout.getNodePosition(fromId);
            Point end = layout.getNodePosition(toId);
            if (start == null || end == null) {
                continue;
            }

            // Alap node félméret (NodeRenderer kör-átmérőhöz igazítva)
            double baseNodeHalfRadius = NodeRenderer.BASE_NODE_DIAMETER / 2.0;

            double dx = end.x - start.x;
            double dy = end.y - start.y;
            double length = Math.sqrt(dx * dx + dy * dy);
            if (length <= 0.0001) {
                continue;
            }

            double ux = dx / length;      // irányvektor X
            double uy = dy / length;      // irányvektor Y
            double nx = -uy;              // normálvektor X
            double ny = ux;               // normálvektor Y

            // Automatikus sáv-köz kiosztás több lane esetén:
            // ne legyen rés a sávok között -> középtávolság = belső vonalvastagság (LaneRenderer: 5px)
            int laneSpacing = 5;
            int count = groupedLanes.size();

            // Lane-köteg teljes szélesség becslés (felső kontúr + összes sáv)
            int laneBodyWidth = 5; // LaneRenderer belső stroke
            int laneOutlineExtra = 1; // körvonal miatt hozzávetőleges extra oldalanként
            int effectiveLaneWidth = laneBodyWidth + (2 * laneOutlineExtra);
            int bundleDiameter = Math.max(NodeRenderer.BASE_NODE_DIAMETER, count * laneSpacing + effectiveLaneWidth);

            // Szabályos kör méret: ahol több út/sáv csatlakozik, ott nő a kör átmérője
            int fromDiameter = Math.max(nodeRequiredWidths.getOrDefault(fromId, NodeRenderer.BASE_NODE_DIAMETER), bundleDiameter);
            int toDiameter = Math.max(nodeRequiredWidths.getOrDefault(toId, NodeRenderer.BASE_NODE_DIAMETER), bundleDiameter);
            nodeRequiredWidths.put(fromId, fromDiameter);
            nodeRequiredWidths.put(toId, toDiameter);

            // Kompatibilitás: height map ugyanazt az értéket kapja, hogy mindenhol kör maradjon
            nodeRequiredHeights.put(fromId, fromDiameter);
            nodeRequiredHeights.put(toId, toDiameter);

            // Start és end node-hoz külön kör-perem metszés (sugárral)
            double fromRadius = Math.max(baseNodeHalfRadius, fromDiameter / 2.0);
            double toRadius = Math.max(baseNodeHalfRadius, toDiameter / 2.0);

            double tFrom = fromRadius;
            double tTo = toRadius;

            // Kis átfedés a node mögé, hogy ne legyen rés a sáv és a csomópont között
            double overlapIntoNode = 3.0;

            for (int i = 0; i < count; i++) {
                GameSnapshot.Entry lane = groupedLanes.get(i);

                // layout offset + automata multi-lane offset kombinálása
                int baseOffset = layout.getLaneOffset(lane.getId());
                double autoOffset = (i - ((count - 1) / 2.0)) * laneSpacing;
                double finalOffset = baseOffset + autoOffset;

                // Alap offsetelt start/end pontok (párhuzamos sávok)
                double startX = start.x + nx * finalOffset;
                double startY = start.y + ny * finalOffset;
                double endX = end.x + nx * finalOffset;
                double endY = end.y + ny * finalOffset;

                // Start pontot előretoljuk, end pontot visszahúzzuk a megfelelő (külön) node pereméhez
                // + kis belógatás a csomópont mögé (résmentes csatlakozás)
                startX += ux * (tFrom + overlapIntoNode);
                startY += uy * (tFrom + overlapIntoNode);
                endX -= ux * (tTo + overlapIntoNode);
                endY -= uy * (tTo + overlapIntoNode);

                Point drawStart = new Point((int) Math.round(startX), (int) Math.round(startY));
                Point drawEnd = new Point((int) Math.round(endX), (int) Math.round(endY));

                RoadLayoutHint roadHint = layout.getRoadHint(roadId);
                if (roadHint == null) {
                    // Java-only fallback: auto alignment döntse el (ne mindig merőleges legyen)
                    roadHint = new RoadLayoutHint(roadId, "curve", 0, "auto");
                }

                boolean isCurve = roadHint.isCurveEnabled();

                if (isCurve) {
                    int curveOffset = roadHint.resolveAutoControlOffset(count, length);

                    // lane-enként kicsit eltérő görbület, hogy több sáv külön is láthatóan ráforduljon a node-ra
                    double laneCurveOffset = curveOffset + (finalOffset * 0.8);

                    // Start/End oldali érintőirány a RoadLayoutHint alapján
                    double[] startTan = roadHint.resolveStartTangent(ux, uy);
                    double[] endTan = roadHint.resolveEndTangent(ux, uy);

                    // Tangenciális kontrollkar hossza + oldalsó görbület
                    double tangentLen = Math.max(14.0, Math.min(length * 0.35, 64.0));
                    double lateral = laneCurveOffset;

                    // Start kontrollpont: indulási érintő + enyhe oldalirányú hajlítás
                    double ctrl1X = drawStart.x + startTan[0] * tangentLen + nx * lateral * 0.35;
                    double ctrl1Y = drawStart.y + startTan[1] * tangentLen + ny * lateral * 0.35;

                    // End kontrollpont: érkezési érintő + enyhe oldalirányú hajlítás
                    double ctrl2X = drawEnd.x + endTan[0] * tangentLen + nx * lateral * 0.35;
                    double ctrl2Y = drawEnd.y + endTan[1] * tangentLen + ny * lateral * 0.35;

                    // Köztes (nem start/end) node kerülése, ha az út túl közel menne hozzá
                    java.awt.geom.Point2D.Double avoidNode = null;
                    double avoidRadius = 0.0;
                    double bestDanger = Double.POSITIVE_INFINITY;

                    java.util.List<GameSnapshot.Entry> allNodes = snapshot.getEntriesByCategory("node");
                    for (GameSnapshot.Entry candidateNode : allNodes) {
                        String candidateId = candidateNode.getId();
                        if (candidateId == null || candidateId.equals(fromId) || candidateId.equals(toId)) {
                            continue;
                        }

                        Point candidatePos = layout.getNodePosition(candidateId);
                        if (candidatePos == null) {
                            continue;
                        }

                        int candidateDiameter = nodeRequiredWidths.getOrDefault(candidateId, NodeRenderer.BASE_NODE_DIAMETER);
                        double candidateRadius = 0.5 * candidateDiameter + 14.0;

                        // Pont-egyenes távolság közelítés a start-end szakaszra
                        double dist = pointToSegmentDistance(
                                candidatePos.x, candidatePos.y,
                                drawStart.x, drawStart.y,
                                drawEnd.x, drawEnd.y
                        );

                        if (dist < candidateRadius && dist < bestDanger) {
                            bestDanger = dist;
                            avoidNode = new java.awt.geom.Point2D.Double(candidatePos.x, candidatePos.y);
                            avoidRadius = candidateRadius;
                        }
                    }

                    if (avoidNode != null) {
                        // Két kerülő oldal jelölt (normál mentén), rövidebb becsült görbehosszt választjuk
                        double avoidPush = Math.max(avoidRadius + 10.0, Math.abs(lateral) + 18.0);

                        double leftCtrl1X = ctrl1X + nx * avoidPush;
                        double leftCtrl1Y = ctrl1Y + ny * avoidPush;
                        double leftCtrl2X = ctrl2X + nx * avoidPush;
                        double leftCtrl2Y = ctrl2Y + ny * avoidPush;

                        double rightCtrl1X = ctrl1X - nx * avoidPush;
                        double rightCtrl1Y = ctrl1Y - ny * avoidPush;
                        double rightCtrl2X = ctrl2X - nx * avoidPush;
                        double rightCtrl2Y = ctrl2Y - ny * avoidPush;

                        double leftScore =
                                dist(drawStart.x, drawStart.y, leftCtrl1X, leftCtrl1Y) +
                                dist(leftCtrl1X, leftCtrl1Y, leftCtrl2X, leftCtrl2Y) +
                                dist(leftCtrl2X, leftCtrl2Y, drawEnd.x, drawEnd.y);

                        double rightScore =
                                dist(drawStart.x, drawStart.y, rightCtrl1X, rightCtrl1Y) +
                                dist(rightCtrl1X, rightCtrl1Y, rightCtrl2X, rightCtrl2Y) +
                                dist(rightCtrl2X, rightCtrl2Y, drawEnd.x, drawEnd.y);

                        if (leftScore <= rightScore) {
                            ctrl1X = leftCtrl1X;
                            ctrl1Y = leftCtrl1Y;
                            ctrl2X = leftCtrl2X;
                            ctrl2Y = leftCtrl2Y;
                        } else {
                            ctrl1X = rightCtrl1X;
                            ctrl1Y = rightCtrl1Y;
                            ctrl2X = rightCtrl2X;
                            ctrl2Y = rightCtrl2Y;
                        }
                    }

                    CubicCurve2D.Double curve = new CubicCurve2D.Double(
                            drawStart.x, drawStart.y,
                            ctrl1X, ctrl1Y,
                            ctrl2X, ctrl2Y,
                            drawEnd.x, drawEnd.y
                    );
                    laneRenderer.render(graphics, lane, curve);
                } else {
                    laneRenderer.render(graphics, lane, drawStart, drawEnd);
                }
            }
        }

        // Csomópontok (Nodes) kirajzolása (hogy fedjék az utakat)
        List<GameSnapshot.Entry> nodes = snapshot.getEntriesByCategory("node");
        for (GameSnapshot.Entry node : nodes) {
            Point pos = layout.getNodePosition(node.getId());
            if (pos != null) {
                int requestedDiameter = nodeRequiredWidths.getOrDefault(node.getId(), NodeRenderer.BASE_NODE_DIAMETER);
                nodeRenderer.render(graphics, node, pos, requestedDiameter);
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
        t = Math.max(0.0, Math.min(1.0, t));

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
