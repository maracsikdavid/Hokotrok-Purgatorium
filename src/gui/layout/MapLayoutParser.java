package gui.layout;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
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
        
        for (int index = 0; index < lines.size(); index++) {
            String line = lines.get(index);
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                parseLine(trimmed, index + 1);
            }
        }

        validateLayout();
        return currentLayout;
    }

    /**
     * Egyetlen layout sor feldolgozásának későbbi helye.
     *
     * @param line a feldolgozandó sor
     * @throws Exception ha a sor formátuma hibás
     */
    public void parseLine(String line) throws LayoutParseException {
        parseLine(line, 0);
    }

    private void parseLine(String line, int lineNumber) throws LayoutParseException {
        if (currentLayout == null) {
            throw new IllegalStateException("A parseLine hívása előtt a currentLayout inicializálása szükséges.");
        }

        String[] parts = splitLine(line);

        if (parts.length == 0) return;

        String type = parts[0];
        switch (type) {
            case "node":
                parseNode(parts, lineNumber, line);
                break;
            case "road":
                parseRoad(parts, lineNumber, line);
                break;
            case "lane":
                parseLane(parts, lineNumber, line);
                break;
            case "roadHint":
                parseRoadHint(parts, lineNumber, line);
                break;
            case "icon":
                parseIcon(parts, lineNumber, line);
                break;
            default:
                throw parseException(lineNumber, "Ismeretlen vagy hiányos layout sor: " + line);
        }
    }

    private String[] splitLine(String line) {
        String[] parts = line.split(",");
        for (int index = 0; index < parts.length; index++) {
            parts[index] = parts[index].trim();
        }
        return parts;
    }

    private void parseNode(String[] parts, int lineNumber, String line) throws LayoutParseException {
        requireLength(parts, 4, lineNumber, line);
        String id = parts[1];
        int x = parseInteger(parts[2], "node x", lineNumber);
        int y = parseInteger(parts[3], "node y", lineNumber);
        currentLayout.putNodePosition(id, new Point(x, y));
        String label = findAttribute(parts, "label", 4);
        if (label != null) {
            currentLayout.putNodeLabel(id, label);
        }
    }

    private void parseRoad(String[] parts, int lineNumber, String line) throws LayoutParseException {
        requireLength(parts, 6, lineNumber, line);
        String roadId = parts[1];
        String from = findAttribute(parts, "from", 2);
        String to = findAttribute(parts, "to", 2);
        if (from == null || to == null) {
            throw parseException(lineNumber, "A road sorból hiányzik a from vagy to mező: " + roadId);
        }
        currentLayout.putRoadEndpoints(roadId, from, to);
        String label = findAttribute(parts, "label", 2);
        if (label != null) {
            currentLayout.putRoadLabel(roadId, label);
        }
    }

    private void parseLane(String[] parts, int lineNumber, String line) throws LayoutParseException {
        requireLength(parts, 4, lineNumber, line);
        String id = parts[1];
        if (!"offset".equals(parts[2])) {
            throw parseException(lineNumber, "A lane sor csak offset attribútumot támogat: " + id);
        }
        int offset = parseInteger(parts[3], "lane offset", lineNumber);
        currentLayout.putLaneOffset(id, offset);
    }

    private void parseRoadHint(String[] parts, int lineNumber, String line) throws LayoutParseException {
        requireLength(parts, 4, lineNumber, line);
        String roadId = parts[1];
        String shape = valueOrDefault(findAttribute(parts, "shape", 2), "straight");
        String nodeAlignment = valueOrDefault(findAttribute(parts, "nodeAlignment", 2), "perpendicular");
        String controlOffsetRaw = findAttribute(parts, "controlOffset", 2);
        int controlOffset = controlOffsetRaw == null ? 0 : parseInteger(controlOffsetRaw, "roadHint controlOffset", lineNumber);
        currentLayout.putRoadHint(new RoadLayoutHint(roadId, shape, controlOffset, nodeAlignment));
    }

    private void parseIcon(String[] parts, int lineNumber, String line) throws LayoutParseException {
        requireLength(parts, 6, lineNumber, line);
        String id = parts[1];
        String iconType = valueOrDefault(findAttribute(parts, "type", 2), "");
        String color = valueOrDefault(findAttribute(parts, "color", 2), "");
        currentLayout.putIconHint(new MapLayout.IconLayoutHint(id, iconType, color));
    }

    private void validateLayout() throws LayoutParseException {
        for (Map.Entry<String, String> roadEntry : currentLayout.getRoadFromNodes().entrySet()) {
            String roadId = roadEntry.getKey();
            String fromNodeId = roadEntry.getValue();
            String toNodeId = currentLayout.getRoadToNode(roadId);
            if (currentLayout.getNodePosition(fromNodeId) == null) {
                throw new LayoutParseException("Layout hiba: a(z) " + roadId
                    + " út ismeretlen kezdő csomópontra hivatkozik: " + fromNodeId);
            }
            if (currentLayout.getNodePosition(toNodeId) == null) {
                throw new LayoutParseException("Layout hiba: a(z) " + roadId
                    + " út ismeretlen cél csomópontra hivatkozik: " + toNodeId);
            }
        }
    }

    private void requireLength(String[] parts, int minimumLength, int lineNumber, String line) throws LayoutParseException {
        if (parts.length < minimumLength) {
            throw parseException(lineNumber, "Hiányos layout sor: " + line);
        }
    }

    private String findAttribute(String[] parts, String attributeName, int startIndex) {
        for (int index = startIndex; index < parts.length - 1; index += 2) {
            if (attributeName.equals(parts[index])) {
                return parts[index + 1];
            }
        }
        return null;
    }

    private String valueOrDefault(String value, String fallback) {
        return value == null ? fallback : value;
    }

    private int parseInteger(String value, String fieldName, int lineNumber) throws LayoutParseException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            throw parseException(lineNumber, "Érvénytelen szám a(z) " + fieldName + " mezőben: " + value);
        }
    }

    private LayoutParseException parseException(int lineNumber, String message) {
        if (lineNumber > 0) {
            return new LayoutParseException("Layout parse hiba a(z) " + lineNumber + ". sorban: " + message);
        }
        return new LayoutParseException("Layout parse hiba: " + message);
    }

    public static class LayoutParseException extends Exception {
        LayoutParseException(String message) {
            super(message);
        }
    }
}