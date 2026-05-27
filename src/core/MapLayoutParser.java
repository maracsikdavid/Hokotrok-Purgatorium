package core;

import java.io.File;
import java.util.Scanner;

/**
 * A pálya layout fájlokat beolvasó és értelmező parser.
 */
public class MapLayoutParser {

    /**
     * Beolvassa és feldolgozza a megadott layout fájlt.
     *
     * @param filePath A beolvasandó layout fájl elérési útja.
     * @return A feldolgozott layout objektum.
     * @throws Exception Ha a fájl nem található vagy hibás a formátum.
     */
    public MapLayout parse(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("Layout file not found: " + filePath);
        }

        MapLayout layout = new MapLayout();
        int lineNumber = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                lineNumber++;
                String rawLine = scanner.nextLine();
                String line = rawLine == null ? "" : rawLine.trim();

                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }

                String[] parts = splitAndTrim(line);
                if (parts.length < 2) {
                    throw new Exception(errorAt(lineNumber, "Invalid layout line format."));
                }

                String type = parts[0];
                switch (type) {
                    case "node":
                        parseNode(layout, parts, lineNumber);
                        break;
                    case "road":
                        parseRoad(layout, parts, lineNumber);
                        break;
                    case "lane":
                        parseLane(layout, parts, lineNumber);
                        break;
                    case "icon":
                        parseIcon(layout, parts, lineNumber);
                        break;
                    default:
                        throw new Exception(errorAt(lineNumber, "Unknown layout entry type: " + type));
                }
            }
        }

        return layout;
    }

    /**
     * Egy csomópont sort dolgoz fel.
     *
     * @param layout A cél layout objektum.
     * @param parts A feldarabolt sor elemei.
     * @param lineNumber Az aktuális sorszám.
     * @throws Exception Ha a sor formátuma hibás.
     */
    private void parseNode(MapLayout layout, String[] parts, int lineNumber) throws Exception {
        if (parts.length < 4) {
            throw new Exception(errorAt(lineNumber, "Invalid node entry."));
        }

        String id = parts[1];
        int x = parseInt(parts[2], lineNumber, "node x");
        int y = parseInt(parts[3], lineNumber, "node y");

        String label = id;
        if (parts.length >= 6 && "label".equals(parts[4])) {
            label = parts[5];
        }

        layout.putNodeHint(new MapLayout.NodeLayoutHint(id, x, y, label));
    }

    /**
     * Egy út sort dolgoz fel.
     *
     * @param layout A cél layout objektum.
     * @param parts A feldarabolt sor elemei.
     * @param lineNumber Az aktuális sorszám.
     * @throws Exception Ha a sor formátuma hibás.
     */
    private void parseRoad(MapLayout layout, String[] parts, int lineNumber) throws Exception {
        if (parts.length < 6) {
            throw new Exception(errorAt(lineNumber, "Invalid road entry."));
        }
        if (!"from".equals(parts[2]) || !"to".equals(parts[4])) {
            throw new Exception(errorAt(lineNumber, "Road entry must contain 'from' and 'to' markers."));
        }

        String id = parts[1];
        String fromNodeId = parts[3];
        String toNodeId = parts[5];
        layout.putRoadHint(new MapLayout.RoadLayoutHint(id, fromNodeId, toNodeId));
    }

    /**
     * Egy sáv sort dolgoz fel.
     *
     * @param layout A cél layout objektum.
     * @param parts A feldarabolt sor elemei.
     * @param lineNumber Az aktuális sorszám.
     * @throws Exception Ha a sor formátuma hibás.
     */
    private void parseLane(MapLayout layout, String[] parts, int lineNumber) throws Exception {
        if (parts.length < 4) {
            throw new Exception(errorAt(lineNumber, "Invalid lane entry."));
        }
        if (!"offset".equals(parts[2])) {
            throw new Exception(errorAt(lineNumber, "Lane entry must contain 'offset' marker."));
        }

        String id = parts[1];
        int offset = parseInt(parts[3], lineNumber, "lane offset");
        layout.putLaneHint(new MapLayout.LaneLayoutHint(id, offset));
    }

    /**
     * Egy ikon sort dolgoz fel.
     *
     * @param layout A cél layout objektum.
     * @param parts A feldarabolt sor elemei.
     * @param lineNumber Az aktuális sorszám.
     * @throws Exception Ha a sor formátuma hibás.
     */
    private void parseIcon(MapLayout layout, String[] parts, int lineNumber) throws Exception {
        if (parts.length < 6) {
            throw new Exception(errorAt(lineNumber, "Invalid icon entry."));
        }
        if (!"type".equals(parts[2]) || !"color".equals(parts[4])) {
            throw new Exception(errorAt(lineNumber, "Icon entry must contain 'type' and 'color' markers."));
        }

        String id = parts[1];
        String type = parts[3];
        String color = parts[5];
        layout.putIconHint(new MapLayout.IconLayoutHint(id, type, color));
    }

    /**
     * Vessző mentén feldarabolja, majd levágja a mezőkről a felesleges szóközöket.
     *
     * @param line A feldolgozandó nyers sor.
     * @return A tisztított mezők tömbje.
     */
    private String[] splitAndTrim(String line) {
        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    /**
     * Számot értelmez szövegből.
     *
     * @param value Az értelmezendő szöveg.
     * @param lineNumber Az aktuális sorszám.
     * @param field A mező neve hibaüzenethez.
     * @return A sikeresen konvertált egész szám.
     * @throws Exception Ha a szöveg nem egész szám.
     */
    private int parseInt(String value, int lineNumber, String field) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new Exception(errorAt(lineNumber, "Invalid number for " + field + ": " + value));
        }
    }

    /**
     * Egységes, sorszámozott parse hibaüzenetet készít.
     *
     * @param lineNumber Az aktuális sorszám.
     * @param message A hiba részletes szövege.
     * @return A kész hibaüzenet.
     */
    private String errorAt(int lineNumber, String message) {
        return "Layout parse error at line " + lineNumber + ": " + message;
    }
}