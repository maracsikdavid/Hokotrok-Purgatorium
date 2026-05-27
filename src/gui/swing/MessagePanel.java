package gui.swing;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Alsó visszajelző panel az utolsó műveletek, hibák és rendszerüzenetek megjelenítéséhez.
 */
public class MessagePanel extends JPanel {
    private final JTextArea messageArea = new JTextArea();

    /**
     * Létrehozza az üzenetpanel alapvető szövegmezőjét.
     */
    public MessagePanel() {
        setLayout(new BorderLayout());
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);
    }

    /**
     * Új üzenetet ad a naplószerű megjelenítéshez.
     *
     * @param message a hozzáadandó üzenet
     */
    public void addMessage(String message) {
        if (message != null && !message.isEmpty()) {
            messageArea.append(message + System.lineSeparator());
        }
    }

    /**
     * Törli az eddig megjelenített üzeneteket.
     */
    public void clear() {
        messageArea.setText("");
    }
}