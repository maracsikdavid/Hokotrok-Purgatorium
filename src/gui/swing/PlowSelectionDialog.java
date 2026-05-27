package gui.swing;

import gui.application.GameSession;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class PlowSelectionDialog extends JDialog {
    private static final Color CARD_BORDER = new Color(17, 24, 39);

    private final transient GameSession session;
    private final transient BiConsumer<String, FeedbackType> statusConsumer;
    private final transient Runnable refreshAction;
    private final String snowplowId;
    private final boolean hasSelectablePlow;
    private String selectedPlowId;

    PlowSelectionDialog(Frame owner, GameSession session, String snowplowId, List<PlowOption> plows,
                        BiConsumer<String, FeedbackType> statusConsumer, Runnable refreshAction) {
        super(owner, "Eke váltás", true);
        this.session = session;
        this.snowplowId = snowplowId;
        this.statusConsumer = statusConsumer;
        this.refreshAction = refreshAction;
        this.hasSelectablePlow = hasSelectablePlow(plows);
        setIconImages(AppIcon.createIconImages());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        JLabel title = new JLabel("Eke váltás", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(14, 16, 8, 16));
        title.setFont(title.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        add(title, BorderLayout.NORTH);

        add(createPlowPanel(plows), BorderLayout.CENTER);

        JButton equipButton = new JButton("Eke felszerelése");
        styleButton(equipButton);
        equipButton.setEnabled(hasSelectablePlow);
        equipButton.addActionListener(event -> equipSelectedPlow());
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 14, 14, 14));
        buttonPanel.add(equipButton, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
    }

    private JPanel createPlowPanel(List<PlowOption> plows) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        if (plows == null || plows.isEmpty()) {
            wrapper.add(new JLabel("Nincs elérhető saját eke.", SwingConstants.CENTER), BorderLayout.CENTER);
            return wrapper;
        }

        JPanel plowPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        plowPanel.setOpaque(false);
        ButtonGroup plowGroup = new ButtonGroup();
        for (PlowOption plow : plows) {
            Color plowColor = GameColors.plowColor(plow.getPlowType());
            Color textColor = GameColors.readableText(plowColor);
            String htmlColor = toHtmlColor(textColor);
            JToggleButton button = new JToggleButton("<html><center><font color='" + htmlColor + "'>" + plow.getDisplayName()
                + "<br><b>" + plow.getId() + "</b>" + (plow.isEquipped() ? "<br>felszerelve" : "")
                + "</font></center></html>");
            button.setPreferredSize(new java.awt.Dimension(150, 80));
            button.setFocusPainted(false);
            button.setOpaque(true);
            button.setContentAreaFilled(true);
            button.setBackground(plowColor);
            button.setForeground(textColor);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 2),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
            if (plow.isEquipped()) {
                button.setEnabled(false);
            }
            button.addActionListener(event -> selectedPlowId = plow.getId());
            plowGroup.add(button);
            plowPanel.add(button);
        }
        wrapper.add(plowPanel, BorderLayout.CENTER);
        if (!hasSelectablePlow) {
            JLabel info = new JLabel("Nincs más választható eke, ez van most rajta.", SwingConstants.CENTER);
            info.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            wrapper.add(info, BorderLayout.SOUTH);
        }
        return wrapper;
    }

    private void equipSelectedPlow() {
        if (selectedPlowId == null || selectedPlowId.isBlank()) {
            if (!hasSelectablePlow) {
                updateStatus("Nincs más választható eke, ez van most rajta.", FeedbackType.INFO);
            } else {
                updateStatus("Válassz ekét a felszereléshez.", FeedbackType.INFO);
            }
            return;
        }
        if (session == null) {
            updateStatus("Az ekeváltás nem futtatható aktív játékmenet nélkül.", FeedbackType.ERROR);
            return;
        }
        try {
            session.equipPlow(snowplowId, selectedPlowId);
            updateStatus("Sikeres ekeváltás: " + selectedPlowId + " felszerelve.", FeedbackType.SUCCESS);
            if (refreshAction != null) {
                refreshAction.run();
            }
            dispose();
        } catch (Exception exception) {
            updateStatus("Sikertelen ekeváltás: " + exception.getMessage(), FeedbackType.ERROR);
        }
    }

    private void updateStatus(String message, FeedbackType type) {
        if (statusConsumer != null) {
            statusConsumer.accept(message, type);
        }
    }

    private static void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        button.setFont(button.getFont().deriveFont(java.awt.Font.BOLD, 12f));
    }

    private String toHtmlColor(Color color) {
        Color resolvedColor = color == null ? Color.BLACK : color;
        return String.format("#%02x%02x%02x", resolvedColor.getRed(), resolvedColor.getGreen(), resolvedColor.getBlue());
    }

    private boolean hasSelectablePlow(List<PlowOption> plows) {
        if (plows == null || plows.isEmpty()) {
            return false;
        }
        for (PlowOption plow : plows) {
            if (!plow.isEquipped()) {
                return true;
            }
        }
        return false;
    }

    static class PlowOption {
        private final String id;
        private final String displayName;
        private final String plowType;
        private final boolean equipped;

        PlowOption(String id, String displayName, String plowType, boolean equipped) {
            this.id = id;
            this.displayName = displayName;
            this.plowType = plowType;
            this.equipped = equipped;
        }

        String getId() {
            return id;
        }

        String getDisplayName() {
            return displayName;
        }

        String getPlowType() {
            return plowType;
        }

        boolean isEquipped() {
            return equipped;
        }
    }
}