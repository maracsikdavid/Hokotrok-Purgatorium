package gui.snapshot;

import actors.Player;
import cli.ObjectRegistry;
import core.Game;
import java.util.Collections;

/**
 * A modell aktuális állapotából egyetlen univerzális {@link GameSnapshot} objektumot készítő factory.
 * A részletes bejegyzés-feltöltés későbbi bővítési pontként marad meg.
 */
public class GameSnapshotFactory {

    /**
     * Elkészíti a játék aktuális GUI-pillanatképét.
     *
     * @param game a fő játékmodell
     * @param registry a központi objektumtár
     * @return a GUI számára átadható univerzális snapshot
     */
    public GameSnapshot createSnapshot(Game game, ObjectRegistry registry) {
        int tickCount = game == null ? 0 : game.getTickCount();
        String currentPlayerId = null;
        if (game != null && registry != null) {
            Player player = game.getCurrentPlayer(registry);
            currentPlayerId = registry.findId(player);
        }
        return new GameSnapshot(tickCount, currentPlayerId, Collections.emptyList(), Collections.emptyList());
    }
}