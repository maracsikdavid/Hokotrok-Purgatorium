package gui.swing;

import gui.application.GameSession;
import gui.snapshot.GameSnapshot;
import java.awt.BorderLayout;
import javax.swing.JPanel;

/**
 * A futó játék fő nézete.
 * Összefogja a térképet, a vezérlőpanelt, a státuszsávot, az üzenetpanelt és a jelmagyarázatot.
 */
public class GamePanel extends JPanel {
    private GameSession session;
    private final MapCanvas mapCanvas = new MapCanvas();
    private final ControlPanel controlPanel = new ControlPanel();
    private final StatusPanel statusPanel = new StatusPanel();
    private final MessagePanel messagePanel = new MessagePanel();
    private final LegendPanel legendPanel = new LegendPanel();
    private final SelectionState selectionState = new SelectionState();

    /**
     * Létrehozza a játéknézet alapvető elrendezését.
     */
    public GamePanel() {
        setLayout(new BorderLayout());
        add(statusPanel, BorderLayout.NORTH);
        add(mapCanvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(messagePanel, BorderLayout.SOUTH);
        add(legendPanel, BorderLayout.WEST);
    }

    /**
     * Összeköti a panelt egy futó grafikus sessionnel.
     *
     * @param session a panelhez tartozó játékmenet
     */
    public void bindSession(GameSession session) {
        this.session = session;
        if (session != null) {
            mapCanvas.setLayout(session.getMapLayout());
            refresh(session.getSnapshot());
        }
    }

    /**
     * Frissíti a teljes panelrendszert az aktuális snapshot alapján.
     *
     * @param snapshot az aktuális játékállapot pillanatképe
     */
    public void refresh(GameSnapshot snapshot) {
        statusPanel.updateStatus(snapshot);
        controlPanel.updateButtons(snapshot);
        mapCanvas.setSnapshot(snapshot);
        mapCanvas.setSelectionState(selectionState);
        messagePanel.clear();
        if (snapshot != null) {
            for (String message : snapshot.getMessages()) {
                messagePanel.addMessage(message);
            }
        }
    }

    /**
     * Visszaadja a panelhez kötött sessiont.
     *
     * @return az aktuális session
     */
    public GameSession getSession() {
        return session;
    }
}