package gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A grafikus alkalmazás kezdőképernyője.
 * Innen választható a konzolos mód, a grafikus mód és az alkalmazás bezárása.
 */
public class StartPanel extends JPanel {
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color PANEL_BORDER = new Color(31, 41, 55);

    private final JButton consoleModeButton = new JButton(SwingActionText.CONSOLE_MODE);
    private final JButton graphicModeButton = new JButton(SwingActionText.GRAPHIC_MODE);
    private final JButton exitButton = new JButton(SwingActionText.EXIT);

    /**
     * Létrehozza a kezdőpanel alapvető komponensvázát.
     */
    public StartPanel() {
        setLayout(new BorderLayout());
        add(createTitlePanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Hókotrók Purgatórium", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 26f));

        JLabel subtitleLabel = new JLabel("Grafikus prototípus", SwingConstants.CENTER);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.BOLD, 16f));

        titlePanel.add(Box.createVerticalStrut(24));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(4));
        titlePanel.add(subtitleLabel);
        return titlePanel;
    }

    private JPanel createButtonPanel() {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        styleMenuButton(consoleModeButton);
        styleMenuButton(graphicModeButton);
        styleMenuButton(exitButton);

        buttonPanel.add(consoleModeButton);
        buttonPanel.add(Box.createVerticalStrut(16));
        buttonPanel.add(graphicModeButton);
        buttonPanel.add(Box.createVerticalStrut(16));
        buttonPanel.add(exitButton);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(48, 0, 0, 0);
        outerPanel.add(buttonPanel, constraints);
        return outerPanel;
    }

    private void styleMenuButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(250, 54));
        button.setMaximumSize(new Dimension(250, 54));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_BORDER, 2),
            BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 15f));
    }

    /**
     * Beállítja a konzolos mód gomb eseménykezelőjét.
     *
     * @param listener a meghívandó eseménykezelő
     */
    public void setConsoleModeListener(ActionListener listener) {
        consoleModeButton.addActionListener(listener);
    }

    /**
     * Beállítja a grafikus mód gomb eseménykezelőjét.
     *
     * @param listener a meghívandó eseménykezelő
     */
    public void setGraphicModeListener(ActionListener listener) {
        graphicModeButton.addActionListener(listener);
    }

    /**
     * Beállítja a kilépés gomb eseménykezelőjét.
     *
     * @param listener a meghívandó eseménykezelő
     */
    public void setExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }
}