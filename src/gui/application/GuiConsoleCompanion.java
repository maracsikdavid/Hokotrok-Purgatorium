package gui.application;

import cli.ConsoleOutput;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.Timer;

/**
 * Konzolos kiseromod, ami a GUI-ban futo jatekkal kozos allapoton dolgozik.
 */
public class GuiConsoleCompanion {
    private static final int INPUT_POLL_INTERVAL_MS = 120;

    private final StringBuilder inputBuffer = new StringBuilder();
    private volatile GameSession activeSession;
    private final AtomicReference<ConsoleCommandListener> commandListener = new AtomicReference<>();
    private volatile boolean started;
    private Timer inputPollTimer;

    public enum CommandFeedbackType {
        SUCCESS,
        ERROR,
        WARNING,
        INFO
    }

    @FunctionalInterface
    public interface ConsoleCommandListener {
        void onCommandFeedback(String command, CommandFeedbackType type, String message);
    }

    /**
     * Elinditja a konzolbemenet figyeleset, ha meg nem fut.
     */
    public synchronized void startIfNeeded() {
        if (started) {
            return;
        }

        started = true;
        inputPollTimer = new Timer(INPUT_POLL_INTERVAL_MS, event -> pollInput());
        inputPollTimer.setRepeats(true);
        inputPollTimer.start();
    }

    /**
     * Aktiv GUI-jatekhoz kapcsolja a konzolt.
     *
     * @param session az aktiv session
     * @param onCommandFeedback GUI-frissitesi callback minden vegrehajtott parancs utan
     */
    public void attachSession(GameSession session, ConsoleCommandListener onCommandFeedback) {
        activeSession = session;
        commandListener.set(onCommandFeedback);
        clearPendingInput();

        if (session == null) {
            return;
        }

        ConsoleOutput.blankLine();
        ConsoleOutput.plain("Üdvözlünk a pályán: " + resolveMapName(session));
        ConsoleOutput.plain("A játék GUI és konzol nézetből is irányítható.");
        ConsoleOutput.help("Elérhető konzol parancsok:");
        ConsoleOutput.help("h/help");
        ConsoleOutput.help("c/command");
        ConsoleOutput.blankLine();
    }

    /**
     * Leválasztja az aktív sessiont konzolos vezérlésről.
     */
    public void clearSession() {
        activeSession = null;
        commandListener.set(null);
        clearPendingInput();
    }

    /**
     * Konzolon jelzi a játék végét és listázza az eredményeket.
     *
     * @param mapName a lezárt pálya megjelenített neve
     * @param resultLines az eredményeket tartalmazó sorok
     */
    public void announceGameFinished(String mapName, List<String> resultLines) {
        String resolvedMapName = mapName == null || mapName.isBlank() ? "ismeretlen pálya" : mapName;
        ConsoleOutput.blankLine();
        ConsoleOutput.help("A játék véget ért ezen a pályán: " + resolvedMapName);
        ConsoleOutput.help("Végeredmény:");
        if (resultLines == null || resultLines.isEmpty()) {
            ConsoleOutput.plain("Nincs elérhető eredmény sor.");
            ConsoleOutput.blankLine();
            return;
        }
        for (String line : resultLines) {
            if (isPlayerResultHeader(line)) {
                ConsoleOutput.blankLine();
                ConsoleOutput.resultPlayer(line);
            } else {
                ConsoleOutput.plain(line);
            }
        }
        ConsoleOutput.blankLine();
    }

    private void pollInput() {
        try {
            int available = System.in.available();
            while (available > 0) {
                int value = System.in.read();
                if (value < 0) {
                    stopPolling();
                    return;
                }
                handleInputByte(value);
                available = System.in.available();
            }
        } catch (IOException exception) {
            ConsoleOutput.error("Konzol bemenet olvasási hiba: " + exception.getMessage());
            stopPolling();
        }
    }

    private synchronized void stopPolling() {
        if (inputPollTimer != null) {
            inputPollTimer.stop();
            inputPollTimer = null;
        }
        started = false;
    }

    private void handleInputByte(int value) {
        if (value == '\r') {
            return;
        }
        if (value == '\n') {
            processCompletedLine(inputBuffer.toString());
            inputBuffer.setLength(0);
            return;
        }
        inputBuffer.append((char) value);
    }

    private void processCompletedLine(String line) {
        String trimmed = normalizeInput(line);
        if (trimmed.isEmpty()) {
            return;
        }

        GameSession session = activeSession;
        if (session == null) {
            return;
        }

        if (handleControlCommand(trimmed)) {
            return;
        }

        executeGameCommand(trimmed);
    }

    private String normalizeInput(String line) {
        if (line == null) {
            return "";
        }
        return line.trim();
    }

    private boolean handleControlCommand(String command) {
        if (isHelpCommand(command)) {
            printHelp();
            return true;
        }
        if (isCommandListRequest(command)) {
            printCommandList();
            return true;
        }
        return false;
    }

    private void executeGameCommand(String command) {
        GameSession session = activeSession;
        if (session == null) {
            return;
        }

        CommandOutputTracker outputTracker = new CommandOutputTracker();
        ConsoleOutput.addOutputListener(outputTracker);
        try {
            session.executeConsoleCommand(command);
            notifyCommandResult(command, outputTracker);
        } catch (RuntimeException exception) {
            String reason = exception.getMessage() == null || exception.getMessage().isBlank()
                ? "ismeretlen hiba"
                : exception.getMessage();
            ConsoleOutput.error(reason);
            notifyCommandFeedback(command, CommandFeedbackType.ERROR,
                "Sikertelen konzolos parancs: " + reason);
        } finally {
            ConsoleOutput.removeOutputListener(outputTracker);
        }
    }

    private void notifyCommandResult(String command, CommandOutputTracker outputTracker) {
        if (outputTracker.hasError()) {
            notifyCommandFeedback(command, CommandFeedbackType.ERROR,
                "Sikertelen konzolos parancs: " + outputTracker.getPrimaryMessage());
            return;
        }
        if (outputTracker.hasWarning()) {
            notifyCommandFeedback(command, CommandFeedbackType.WARNING,
                "Figyelmeztetés konzolos parancsnál: " + outputTracker.getPrimaryMessage());
            return;
        }

        ConsoleOutput.success("Konzolos parancs sikeres: " + command);
        notifyCommandFeedback(command, CommandFeedbackType.SUCCESS,
            "Konzolos parancs sikeres: " + command);
    }

    private void notifyCommandFeedback(String command, CommandFeedbackType type, String message) {
        ConsoleCommandListener listener = commandListener.get();
        if (listener != null) {
            listener.onCommandFeedback(command, type, message);
        }
    }

    private void printHelp() {
        ConsoleOutput.blankLine();
        ConsoleOutput.help("c/command - Játékmód parancsok listája");
        ConsoleOutput.help("h/help - Súgó");
        ConsoleOutput.blankLine();
    }

    private void printCommandList() {
        GameSession session = activeSession;
        String role = session == null ? "Unknown" : session.getCurrentPlayerRole();

        ConsoleOutput.blankLine();
        ConsoleOutput.help("s - Display the current player's status");
        ConsoleOutput.help("w - Show where the player can move next");
        if ("BusDriver".equals(role)) {
            ConsoleOutput.help("bus,<busID>,<roadID>,<laneID>");
        } else {
            ConsoleOutput.help("buy,<shopID>,<ShopItem>");
            ConsoleOutput.help("equip,<snowplowID>,<plowID>");
            ConsoleOutput.help("refill,<snowplowID>,<consumableID>");
            ConsoleOutput.help("plow,<snowplowID>,<roadID>,<laneID>");
        }
        ConsoleOutput.blankLine();
    }

    private boolean isPlayerResultHeader(String line) {
        return line != null && line.matches("\\d+\\. .+\\(.+\\)");
    }

    private static class CommandOutputTracker implements ConsoleOutput.OutputListener {
        private boolean error;
        private boolean warning;
        private String primaryMessage = "ismeretlen állapot";

        @Override
        public void onOutput(ConsoleOutput.OutputType type, String message) {
            if (type == ConsoleOutput.OutputType.ERROR) {
                error = true;
                primaryMessage = normalizeMessage(message);
            } else if (type == ConsoleOutput.OutputType.WARNING && !error) {
                warning = true;
                primaryMessage = normalizeMessage(message);
            }
        }

        boolean hasError() {
            return error;
        }

        boolean hasWarning() {
            return warning;
        }

        String getPrimaryMessage() {
            return primaryMessage;
        }

        private String normalizeMessage(String message) {
            return message == null || message.isBlank() ? "ismeretlen állapot" : message;
        }
    }

    private boolean isHelpCommand(String command) {
        return "h".equalsIgnoreCase(command) || "help".equalsIgnoreCase(command);
    }

    private boolean isCommandListRequest(String command) {
        return "c".equalsIgnoreCase(command) || "command".equalsIgnoreCase(command);
    }

    private void clearPendingInput() {
        inputBuffer.setLength(0);
    }

    private String resolveMapName(GameSession session) {
        if (session == null || session.getMapDescriptor() == null) {
            return "ismeretlen pálya";
        }
        return session.getMapDescriptor().getDisplayName();
    }
}