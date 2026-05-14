package gui.application;

import cli.ObjectRegistry;
import cli.Parser;
import core.Game;
import core.GameRules;
import gui.layout.MapLayout;
import gui.layout.MapLayoutParser;
import java.nio.file.Paths;

/**
 * Új grafikus játékmenet létrehozásáért felelős factory.
 * A kiválasztott pályaleírót parserrel, regiszterrel, modellel és layouttal kapcsolja össze.
 */
public class GameSessionFactory {

    /**
     * Létrehozza a kiválasztott pályához tartozó grafikus sessiont.
     *
     * @param descriptor a kiválasztott pálya leírója
     * @return a felépített grafikus session
     * @throws Exception ha a session előkészítése közben hiba történik
     */
    public GameSession createSession(MapDescriptor descriptor) throws Exception {
        String previousMap = GameRules.mapFileName;
        if (descriptor != null && descriptor.getInitFilePath() != null) {
            GameRules.mapFileName = descriptor.getInitFilePath();
        }

        Parser parser;
        try {
            parser = new Parser(1);
        } finally {
            GameRules.mapFileName = previousMap;
        }

        ObjectRegistry registry = parser.getRegistry();
        Game game = parser.getPrimaryGame();
        MapLayout layout = new MapLayout();
        if (descriptor != null && descriptor.getLayoutFilePath() != null) {
            layout = new MapLayoutParser().parse(Paths.get(descriptor.getLayoutFilePath()));
        }
        GameCommandService commandService = new GameCommandService(registry, game);
        return new GameSession(parser, registry, game, descriptor, layout, commandService);
    }
}