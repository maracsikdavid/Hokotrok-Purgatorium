package gui.snapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A teljes játékállapot egyetlen, univerzális GUI-pillanatképe.
 * Minden kirajzolható vagy megjeleníthető elem azonos szerkezetű {@link Entry} bejegyzésként szerepel benne.
 */
public class GameSnapshot {
    private final int tickCount;
    private final String currentPlayerId;
    private final List<Entry> entries;
    private final List<String> messages;

    /**
     * Konstruktor az univerzális snapshot alapadatainak beállításához.
     *
     * @param tickCount az eltelt tickek száma
     * @param currentPlayerId az aktuális játékos azonosítója
     * @param entries a snapshot univerzális bejegyzései
     * @param messages a GUI-n megjeleníthető üzenetek
     */
    public GameSnapshot(int tickCount, String currentPlayerId, List<Entry> entries, List<String> messages) {
        this.tickCount = tickCount;
        this.currentPlayerId = currentPlayerId;
        this.entries = entries == null ? new ArrayList<>() : new ArrayList<>(entries);
        this.messages = messages == null ? new ArrayList<>() : new ArrayList<>(messages);
    }

    /**
     * Visszaadja az eltelt tickek számát.
     *
     * @return a tick számláló értéke
     */
    public int getTickCount() {
        return tickCount;
    }

    /**
     * Visszaadja az aktuális játékos azonosítóját.
     *
     * @return az aktuális játékos azonosítója
     */
    public String getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * Visszaadja az összes univerzális snapshot bejegyzést.
     *
     * @return bejegyzések csak olvasható listája
     */
    public List<Entry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Visszaadja a megjeleníthető üzeneteket.
     *
     * @return üzenetek csak olvasható listája
     */
    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    /**
     * Kategória alapján kiszűri a snapshot bejegyzéseit.
     *
     * @param category a keresett kategória, például "node", "lane", "vehicle" vagy "player"
     * @return az egyező bejegyzések listája
     */
    public List<Entry> getEntriesByCategory(String category) {
        List<Entry> result = new ArrayList<>();
        for (Entry entry : entries) {
            if (entry != null && equals(category, entry.getCategory())) {
                result.add(entry);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Azonosító alapján megkeres egy snapshot bejegyzést.
     *
     * @param id a keresett bejegyzés azonosítója
     * @return a megtalált bejegyzés, vagy null ha nincs ilyen
     */
    public Entry findEntryById(String id) {
        for (Entry entry : entries) {
            if (entry != null && equals(id, entry.getId())) {
                return entry;
            }
        }
        return null;
    }

    private static boolean equals(String left, String right) {
        return left == null ? right == null : left.equals(right);
    }

    /**
     * Egyetlen univerzális snapshot elem.
     * A kategória és a típus mondja meg, hogy játékosról, útról, sávról, járműről vagy boltról van-e szó.
     */
    public static class Entry {
        private final String id;
        private final String category;
        private final String type;
        private final String label;
        private final Map<String, String> attributes;

        /**
         * Konstruktor az univerzális snapshot bejegyzés alapadatainak beállításához.
         *
         * @param id az elem azonosítója
         * @param category az elem fő kategóriája
         * @param type az elem konkrét típusa
         * @param label a felületen megjeleníthető címke
         * @param attributes további kulcs-érték adatok
         */
        public Entry(String id, String category, String type, String label, Map<String, String> attributes) {
            this.id = id;
            this.category = category;
            this.type = type;
            this.label = label;
            this.attributes = attributes == null ? new LinkedHashMap<>() : new LinkedHashMap<>(attributes);
        }

        /**
         * Visszaadja az elem azonosítóját.
         *
         * @return az elem azonosítója
         */
        public String getId() {
            return id;
        }

        /**
         * Visszaadja az elem fő kategóriáját.
         *
         * @return az elem kategóriája
         */
        public String getCategory() {
            return category;
        }

        /**
         * Visszaadja az elem konkrét típusát.
         *
         * @return az elem típusa
         */
        public String getType() {
            return type;
        }

        /**
         * Visszaadja az elem megjeleníthető címkéjét.
         *
         * @return az elem címkéje
         */
        public String getLabel() {
            return label;
        }

        /**
         * Visszaadja az elem további attribútumait.
         *
         * @return attribútumok csak olvasható mapje
         */
        public Map<String, String> getAttributes() {
            return Collections.unmodifiableMap(attributes);
        }

        /**
         * Lekér egy attribútumot kulcs alapján.
         *
         * @param key a keresett attribútum neve
         * @return az attribútum értéke, vagy null ha nincs ilyen kulcs
         */
        public String getAttribute(String key) {
            return attributes.get(key);
        }
    }
}