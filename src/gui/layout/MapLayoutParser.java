package gui.layout;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.awt.Point;

/**
 * A grafikus layout fájlok feldolgozásának belépési pontja.
 * A részletes parse logika későbbi implementációra van előkészítve.
 */
public class MapLayoutParser {

    private MapLayout currentLayout;
    /**
     * Feldolgozza a megadott layout fájlt és visszaadja a hozzá tartozó layout objektumot.
     *
     * @param file a feldolgozandó layout fájl útvonala
     * @return az elkészített layout objektum
     * @throws Exception ha a későbbi parse folyamat hibát talál
     */
    public MapLayout parse(Path file) throws Exception {
        currentLayout = new MapLayout();
        List<String> lines = Files.readAllLines(file);
        
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                parseLine(trimmed);
            }
        }
        
        return currentLayout;
    }

    /**
     * Egyetlen layout sor feldolgozásának későbbi helye.
     *
     * @param line a feldolgozandó sor
     * @throws Exception ha a sor formátuma hibás
     */
    public void parseLine(String line) throws Exception {
        if (currentLayout == null) {
            throw new IllegalStateException("A parseLine hívása előtt a currentLayout inicializálása szükséges.");
        }

        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        if (parts.length == 0) return;

        String type = parts[0];

        if ("node".equals(type) && parts.length >= 4) {
            String id = parts[1];
            int x = Integer.parseInt(parts[2]);
            int y = Integer.parseInt(parts[3]);
            currentLayout.putNodePosition(id, new Point(x, y));
            
        } else if ("road".equals(type) && parts.length >= 6) {
            String roadId = parts[1];
            String from = null;
            String to = null;

            for (int i = 2; i < parts.length - 1; i += 2) {
                if ("from".equals(parts[i])) from = parts[i + 1];
                if ("to".equals(parts[i])) to = parts[i + 1];
            }

            if (from != null && to != null) {
                currentLayout.putRoadEndpoints(roadId, from, to);
            }

        } else if ("lane".equals(type) && parts.length >= 4) {
            String id = parts[1];
            if ("offset".equals(parts[2])) {
                int offset = Integer.parseInt(parts[3]);
                currentLayout.putLaneOffset(id, offset);
            }

        } else if ("roadHint".equals(type) && parts.length >= 4) {
            String roadId = parts[1];
            String shape = "straight";
            int controlOffset = 0;
            String nodeAlignment = "perpendicular";

            for (int i = 2; i < parts.length - 1; i += 2) {
                if ("shape".equals(parts[i])) {
                    shape = parts[i + 1];
                } else if ("controlOffset".equals(parts[i])) {
                    controlOffset = Integer.parseInt(parts[i + 1]);
                } else if ("nodeAlignment".equals(parts[i])) {
                    nodeAlignment = parts[i + 1];
                }
            }

            currentLayout.putRoadHint(new RoadLayoutHint(roadId, shape, controlOffset, nodeAlignment));

        } else if ("icon".equals(type) && parts.length >= 6) {
            String id = parts[1];
            String iconType = "";
            String color = "";
            
            for (int i = 2; i < parts.length - 1; i += 2) {
                if ("type".equals(parts[i])) iconType = parts[i + 1];
                if ("color".equals(parts[i])) color = parts[i + 1];
            }
            
            currentLayout.putIconHint(new MapLayout.IconLayoutHint(id, iconType, color));
        }
    }
}