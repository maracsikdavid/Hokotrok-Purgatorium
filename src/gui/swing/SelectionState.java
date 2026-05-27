package gui.swing;

/**
 * A grafikus felület ideiglenes kijelölési állapotát tárolja.
 * Ez az állapot kizárólag a nézethez tartozik, ezért nem kerül be a modellrétegbe.
 */
public class SelectionState {
    private String selectedVehicleId;
    private String selectedRoadId;
    private String selectedLaneId;
    private String hoveredElementId;

    /**
     * Visszaadja a kijelölt jármű azonosítóját.
     *
     * @return a kijelölt jármű azonosítója
     */
    public String getSelectedVehicleId() {
        return selectedVehicleId;
    }

    /**
     * Beállítja a kijelölt jármű azonosítóját.
     *
     * @param selectedVehicleId a kijelölt jármű azonosítója
     */
    public void setSelectedVehicleId(String selectedVehicleId) {
        this.selectedVehicleId = selectedVehicleId;
    }

    /**
     * Visszaadja a kijelölt út azonosítóját.
     *
     * @return a kijelölt út azonosítója
     */
    public String getSelectedRoadId() {
        return selectedRoadId;
    }

    /**
     * Beállítja a kijelölt út azonosítóját.
     *
     * @param selectedRoadId a kijelölt út azonosítója
     */
    public void setSelectedRoadId(String selectedRoadId) {
        this.selectedRoadId = selectedRoadId;
    }

    /**
     * Visszaadja a kijelölt sáv azonosítóját.
     *
     * @return a kijelölt sáv azonosítója
     */
    public String getSelectedLaneId() {
        return selectedLaneId;
    }

    /**
     * Beállítja a kijelölt sáv azonosítóját.
     *
     * @param selectedLaneId a kijelölt sáv azonosítója
     */
    public void setSelectedLaneId(String selectedLaneId) {
        this.selectedLaneId = selectedLaneId;
    }

    /**
     * Visszaadja az egér alatt lévő elem azonosítóját.
     *
     * @return a hoverelt elem azonosítója
     */
    public String getHoveredElementId() {
        return hoveredElementId;
    }

    /**
     * Beállítja az egér alatt lévő elem azonosítóját.
     *
     * @param hoveredElementId a hoverelt elem azonosítója
     */
    public void setHoveredElementId(String hoveredElementId) {
        this.hoveredElementId = hoveredElementId;
    }

    /**
     * Törli az összes ideiglenes kijelölést.
     */
    public void clear() {
        selectedVehicleId = null;
        selectedRoadId = null;
        selectedLaneId = null;
        hoveredElementId = null;
    }
}