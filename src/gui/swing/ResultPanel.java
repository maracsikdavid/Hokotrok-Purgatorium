package gui.swing;

import gui.snapshot.GameSnapshot;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 * A játék kézzel lezárt végállapotát és az új játék/kilépés műveleteket megjelenítő panel.
 */
public class ResultPanel extends JPanel {
    private static final Color BACKGROUND = new Color(248, 250, 252);
    private static final Color PANEL_BORDER = new Color(17, 24, 39);

    private final JLabel titleLabel = new JLabel("Végső állapot", SwingConstants.CENTER);
    private final JLabel subtitleLabel = new JLabel("Játékosok eredményei", SwingConstants.CENTER);
    private final JPanel resultsListPanel = new JPanel();
    private final JButton newGameButton = new JButton(SwingActionText.NEW_GAME);
    private final JButton exitButton = new JButton(SwingActionText.EXIT);

    /**
     * Létrehozza az eredménypanel alapvető komponenseit.
     */
    public ResultPanel() {
        setLayout(new BorderLayout(18, 18));
        setBackground(BACKGROUND);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 3),
            BorderFactory.createEmptyBorder(42, 64, 42, 64)));

        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
        subtitleLabel.setFont(subtitleLabel.getFont().deriveFont(Font.BOLD, 14f));
        resultsListPanel.setLayout(new BoxLayout(resultsListPanel, BoxLayout.Y_AXIS));
        resultsListPanel.setBackground(Color.WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createResultsBox(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    /**
     * Frissíti az eredménypanel adatait.
     *
     * @param snapshot a játék végállapotának pillanatképe
     */
    public void refresh(GameSnapshot snapshot) {
        String currentPlayer = snapshot == null ? "-" : snapshot.getCurrentPlayerId();
        int tickCount = snapshot == null ? 0 : snapshot.getTickCount();
        setPlayerResults(List.of(new PlayerResult(currentPlayer, "-", List.of("Tick: " + tickCount))));
    }

    /**
     * Frissiti az eredmenyoldalt a prototipusban regisztralt jatekosokkal.
     *
     * @param players regisztralt jatekosok
     */
    public void setResults(List<PlayerRegisterPanel.RegisteredPlayer> players) {
        List<PlayerResult> results = new ArrayList<>();
        if (players != null) {
            for (PlayerRegisterPanel.RegisteredPlayer player : players) {
                List<String> details = List.of(
                    player.getPlayerRole().getScoreLabel() + ": " + player.getPlayerRole().getInitialValue());
                results.add(new PlayerResult(player.getName(), player.getRoleDisplayName(), details));
            }
        }
        setPlayerResults(results);
    }

    public void setPlayerResults(List<PlayerResult> results) {
        resultsListPanel.removeAll();
        if (results == null || results.isEmpty()) {
            resultsListPanel.add(createEmptyResultLabel());
        } else {
            for (int index = 0; index < results.size(); index++) {
                resultsListPanel.add(createResultRow(results.get(index), index < results.size() - 1));
            }
        }
        resultsListPanel.revalidate();
        resultsListPanel.repaint();
    }

    /**
     * Beállítja az új játék gomb eseménykezelőjét.
     *
     * @param listener az új játék indításakor meghívandó kezelő
     */
    public void setNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    /**
     * Beállítja a kilépés gomb eseménykezelőjét.
     *
     * @param listener a kilépéskor meghívandó kezelő
     */
    public void setExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 8));
        header.setOpaque(false);
        header.add(titleLabel);
        header.add(subtitleLabel);
        return header;
    }

    private JPanel createResultsBox() {
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel resultBox = new JPanel(new BorderLayout());
        resultBox.setBackground(Color.WHITE);
        resultBox.setPreferredSize(new Dimension(430, 280));
        resultBox.setBorder(BorderFactory.createLineBorder(PANEL_BORDER, 3));

        JScrollPane scrollPane = new JScrollPane(resultsListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        resultBox.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0, 0, 0, 0);
        centerWrapper.add(resultBox, constraints);
        return centerWrapper;
    }

    private JPanel createButtonPanel() {
        JPanel footerWrapper = new JPanel(new GridBagLayout());
        footerWrapper.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(430, 42));
        styleButton(newGameButton);
        styleButton(exitButton);
        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);

        footerWrapper.add(buttonPanel, new GridBagConstraints());
        return footerWrapper;
    }

    private Component createEmptyResultLabel() {
        JLabel emptyLabel = new JLabel("Nincs rögzített játékos.", SwingConstants.CENTER);
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(22, 12, 22, 12));
        emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return emptyLabel;
    }

    private JPanel createResultRow(PlayerResult result, boolean withSeparator) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setBorder(BorderFactory.createCompoundBorder(
            withSeparator ? BorderFactory.createMatteBorder(0, 0, 2, 0, PANEL_BORDER) : BorderFactory.createEmptyBorder(),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)));

        JLabel playerLabel = new JLabel(result.getPlayerName() + " (" + result.getRoleDisplayName() + ")");
        playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD, 13f));
        playerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(playerLabel);
        row.add(Box.createVerticalStrut(6));

        for (String detail : result.getDetails()) {
            JLabel detailLabel = new JLabel(detail);
            detailLabel.setFont(detailLabel.getFont().deriveFont(Font.BOLD, 12f));
            detailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.add(detailLabel);
            row.add(Box.createVerticalStrut(4));
        }
        return row;
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PANEL_BORDER, 2),
            BorderFactory.createEmptyBorder(9, 18, 9, 18)));
        button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
    }

    public static class PlayerResult {
        private final String playerName;
        private final String roleDisplayName;
        private final List<String> details;

        public PlayerResult(String playerName, String roleDisplayName, List<String> details) {
            this.playerName = playerName == null || playerName.isBlank() ? "Játékos" : playerName;
            this.roleDisplayName = roleDisplayName == null || roleDisplayName.isBlank() ? "-" : roleDisplayName;
            this.details = details == null ? new ArrayList<>() : new ArrayList<>(details);
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getRoleDisplayName() {
            return roleDisplayName;
        }

        public List<String> getDetails() {
            return Collections.unmodifiableList(details);
        }
    }
}