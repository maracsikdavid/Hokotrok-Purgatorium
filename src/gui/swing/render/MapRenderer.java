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

            // Alap node félméretek (NodeRenderer-hez igazítva: 28x14)
            double baseNodeHalfWidth = NodeRenderer.BASE_NODE_WIDTH / 2.0;
            double baseNodeHalfHeight = NodeRenderer.BASE_NODE_HEIGHT / 2.0;

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
            int bundleWidth = Math.max(NodeRenderer.BASE_NODE_WIDTH, count * laneSpacing + effectiveLaneWidth);

            // Keresztirányú nyújtás:
            // - vízszintes érkezésnél függőlegesen nyújtunk
            // - függőleges érkezésnél vízszintesen nyújtunk
            if (Math.abs(ux) >= Math.abs(uy)) {
                nodeRequiredHeights.put(fromId, Math.max(nodeRequiredHeights.getOrDefault(fromId, NodeRenderer.BASE_NODE_HEIGHT), bundleWidth));
                nodeRequiredHeights.put(toId, Math.max(nodeRequiredHeights.getOrDefault(toId, NodeRenderer.BASE_NODE_HEIGHT), bundleWidth));
                nodeRequiredWidths.putIfAbsent(fromId, NodeRenderer.BASE_NODE_WIDTH);
                nodeRequiredWidths.putIfAbsent(toId, NodeRenderer.BASE_NODE_WIDTH);
            } else {
                nodeRequiredWidths.put(fromId, Math.max(nodeRequiredWidths.getOrDefault(fromId, NodeRenderer.BASE_NODE_WIDTH), bundleWidth));
                nodeRequiredWidths.put(toId, Math.max(nodeRequiredWidths.getOrDefault(toId, NodeRenderer.BASE_NODE_WIDTH), bundleWidth));
                nodeRequiredHeights.putIfAbsent(fromId, NodeRenderer.BASE_NODE_HEIGHT);
                nodeRequiredHeights.putIfAbsent(toId, NodeRenderer.BASE_NODE_HEIGHT);
            }

            int fromWidth = nodeRequiredWidths.getOrDefault(fromId, NodeRenderer.BASE_NODE_WIDTH);
            int fromHeight = nodeRequiredHeights.getOrDefault(fromId, NodeRenderer.BASE_NODE_HEIGHT);
            int toWidth = nodeRequiredWidths.getOrDefault(toId, NodeRenderer.BASE_NODE_WIDTH);
            int toHeight = nodeRequiredHeights.getOrDefault(toId, NodeRenderer.BASE_NODE_HEIGHT);

            // Start és end node-hoz külön dinamikus perem-metszés
            double fromHalfWidth = Math.max(baseNodeHalfWidth, fromWidth / 2.0);
            double fromHalfHeight = Math.max(baseNodeHalfHeight, fromHeight / 2.0);
            double toHalfWidth = Math.max(baseNodeHalfWidth, toWidth / 2.0);
            double toHalfHeight = Math.max(baseNodeHalfHeight, toHeight / 2.0);

            double tFrom = 1.0 / Math.sqrt(
                    (ux * ux) / (fromHalfWidth * fromHalfWidth)
                            + (uy * uy) / (fromHalfHeight * fromHalfHeight));
            double tTo = 1.0 / Math.sqrt(
                    (ux * ux) / (toHalfWidth * toHalfWidth)
                            + (uy * uy) / (toHalfHeight * toHalfHeight));

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
                int requestedWidth = nodeRequiredWidths.getOrDefault(node.getId(), NodeRenderer.BASE_NODE_WIDTH);
                int requestedHeight = nodeRequiredHeights.getOrDefault(node.getId(), NodeRenderer.BASE_NODE_HEIGHT);
                nodeRenderer.render(graphics, node, pos, requestedWidth, requestedHeight);
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
}