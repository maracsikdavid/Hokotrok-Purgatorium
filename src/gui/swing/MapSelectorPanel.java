package gui.swing;

import gui.application.MapCatalog;
import gui.application.MapDescriptor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

/**
 * A specifikacio szerinti palyavalaszto panel negy kartya-elonezettel.
 */
public class MapSelectorPanel extends JPanel {
	private static final Color BACKGROUND = new Color(248, 250, 252);
	private static final Color CARD_BORDER = new Color(17, 24, 39);
	private static final Color PREVIEW_LINE = new Color(30, 64, 175);

	private final JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
	private final JButton backButton = new JButton(SwingActionText.BACK_TO_MENU);
	private MapCatalog catalog;
	private transient ActionListener selectionListener;
	private transient ActionListener backListener;

	/**
	 * Letrehozza a palyavalaszto kepernyot.
	 */
	public MapSelectorPanel() {
		setLayout(new BorderLayout(0, 24));
		setBackground(BACKGROUND);
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(Color.BLACK, 3),
			BorderFactory.createEmptyBorder(44, 42, 32, 42)));

		add(createHeader(), BorderLayout.NORTH);
		add(createCardsWrapper(), BorderLayout.CENTER);
		add(createFooter(), BorderLayout.SOUTH);

		backButton.addActionListener(event -> {
			if (backListener != null) {
				backListener.actionPerformed(event);
			}
		});
	}

	/**
	 * Osszekoti a panelt a megjelenitendo palyakatalogussal.
	 *
	 * @param catalog a palyakatalogus
	 */
	public void bindCatalog(MapCatalog catalog) {
		this.catalog = catalog;
		rebuildCards();
	}

	/**
	 * Beallitja a palyavalasztasi esemenykezelojet.
	 *
	 * @param listener a kivalasztaskor meghivando kezelo
	 */
	public void setSelectionListener(ActionListener listener) {
		this.selectionListener = listener;
	}

	/**
	 * Beallitja a vissza gomb esemenykezelojet.
	 *
	 * @param listener a visszalepeskor meghivando kezelo
	 */
	public void setBackListener(ActionListener listener) {
		this.backListener = listener;
	}

	private JPanel createHeader() {
		JPanel header = new JPanel();
		header.setOpaque(false);
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

		JLabel title = new JLabel("Üdvözlünk a pálya választóban!", SwingConstants.CENTER);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));

		JLabel subtitle = new JLabel("Válassz pályát!", SwingConstants.CENTER);
		subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		subtitle.setFont(subtitle.getFont().deriveFont(Font.BOLD, 14f));

		header.add(title);
		header.add(Box.createVerticalStrut(8));
		header.add(subtitle);
		return header;
	}

	private JPanel createCardsWrapper() {
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		cardsPanel.setOpaque(false);
		wrapper.add(cardsPanel, BorderLayout.CENTER);
		return wrapper;
	}

	private JPanel createFooter() {
		JPanel footer = new JPanel(new BorderLayout());
		footer.setOpaque(false);
		styleButton(backButton);
		footer.add(backButton, BorderLayout.EAST);
		return footer;
	}

	private void rebuildCards() {
		cardsPanel.removeAll();
		if (catalog != null) {
			for (MapDescriptor descriptor : catalog.getAllMaps()) {
				cardsPanel.add(createMapCard(descriptor));
			}
		}
		revalidate();
		repaint();
	}

	private JPanel createMapCard(MapDescriptor descriptor) {
		JPanel card = new JPanel(new BorderLayout(0, 10));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createLineBorder(CARD_BORDER, 3));
		card.setPreferredSize(new Dimension(180, 280));

		JLabel title = new JLabel(toCardTitle(descriptor.getDisplayName()), SwingConstants.CENTER);
		title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
		title.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, CARD_BORDER));
		card.add(title, BorderLayout.NORTH);

		JPanel preview = new MapPreviewPanel(descriptor.getId());
		preview.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		preview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		preview.setToolTipText("Pálya kiválasztása");
		preview.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				fireSelection(descriptor);
			}
		});
		card.add(preview, BorderLayout.CENTER);

		card.add(createMetadataPanel(descriptor), BorderLayout.SOUTH);
		return card;
	}

	private JPanel createMetadataPanel(MapDescriptor descriptor) {
		JPanel metadataPanel = new JPanel(new BorderLayout(0, 6));
		metadataPanel.setOpaque(false);
		metadataPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		String difficulty = descriptor.getDifficulty() == null || descriptor.getDifficulty().isBlank()
			? "Nincs megadva" : descriptor.getDifficulty();
		JLabel difficultyLabel = new JLabel("Nehézség: " + difficulty, SwingConstants.CENTER);
		difficultyLabel.setFont(difficultyLabel.getFont().deriveFont(Font.BOLD, 12f));

		JTextArea descriptionArea = new JTextArea(descriptor.getDescription());
		descriptionArea.setEditable(false);
		descriptionArea.setFocusable(false);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setRows(3);
		descriptionArea.setOpaque(false);
		descriptionArea.setFont(descriptionArea.getFont().deriveFont(12f));
		descriptionArea.setBorder(BorderFactory.createEmptyBorder());

		metadataPanel.add(difficultyLabel, BorderLayout.NORTH);
		metadataPanel.add(descriptionArea, BorderLayout.CENTER);
		return metadataPanel;
	}

	private void fireSelection(MapDescriptor descriptor) {
		if (selectionListener != null && descriptor != null) {
			selectionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, descriptor.getId()));
		}
	}

	private static String toCardTitle(String displayName) {
		if (displayName == null || displayName.isBlank()) {
			return "Pálya";
		}
		int separatorIndex = displayName.lastIndexOf(" - ");
		return separatorIndex >= 0 ? displayName.substring(separatorIndex + 3) : displayName;
	}

	private static void styleButton(JButton button) {
		button.setFocusPainted(false);
		button.setBackground(Color.WHITE);
		button.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(CARD_BORDER, 2),
			BorderFactory.createEmptyBorder(8, 18, 8, 18)));
		button.setFont(button.getFont().deriveFont(Font.BOLD, 13f));
	}

	private static class MapPreviewPanel extends JPanel {
		private final String mapId;

		MapPreviewPanel(String mapId) {
			this.mapId = mapId == null ? "" : mapId;
			setBackground(new Color(241, 245, 249));
			setPreferredSize(new Dimension(150, 135));
		}

		@Override
		protected void paintComponent(Graphics graphics) {
			super.paintComponent(graphics);
			Graphics2D graphics2D = (Graphics2D) graphics.create();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.setColor(PREVIEW_LINE);
			graphics2D.setStroke(new java.awt.BasicStroke(3f));

			int width = getWidth();
			int height = getHeight();
			if ("tutorial".equals(mapId)) {
				graphics2D.drawLine(24, height / 2, width - 24, height / 2);
				drawNode(graphics2D, 24, height / 2);
				drawNode(graphics2D, width - 24, height / 2);
			} else if ("city".equals(mapId)) {
				drawCrossPreview(graphics2D, width, height);
			} else if ("blizzard".equals(mapId)) {
				drawCrossPreview(graphics2D, width, height);
				graphics2D.setColor(new Color(14, 165, 233));
				graphics2D.drawOval(width / 2 - 34, height / 2 - 34, 68, 68);
			} else {
				drawLoopPreview(graphics2D, width, height);
			}
			graphics2D.dispose();
		}

		private static void drawLoopPreview(Graphics2D graphics2D, int width, int height) {
			int[][] points = {{28, 34}, {width - 30, 30}, {width - 42, height - 32}, {width / 2, height / 2}, {32, height - 34}};
			for (int index = 0; index < points.length; index++) {
				int[] current = points[index];
				int[] next = points[(index + 1) % points.length];
				graphics2D.drawLine(current[0], current[1], next[0], next[1]);
			}
			for (int[] point : points) {
				drawNode(graphics2D, point[0], point[1]);
			}
		}

		private static void drawCrossPreview(Graphics2D graphics2D, int width, int height) {
			graphics2D.drawLine(28, 28, width - 28, height - 28);
			graphics2D.drawLine(width - 28, 28, 28, height - 28);
			drawNode(graphics2D, 28, 28);
			drawNode(graphics2D, width - 28, 28);
			drawNode(graphics2D, width / 2, height / 2);
			drawNode(graphics2D, 28, height - 28);
			drawNode(graphics2D, width - 28, height - 28);
		}

		private static void drawNode(Graphics2D graphics2D, int x, int y) {
			graphics2D.setColor(Color.WHITE);
			graphics2D.fillOval(x - 10, y - 10, 20, 20);
			graphics2D.setColor(PREVIEW_LINE);
			graphics2D.drawOval(x - 10, y - 10, 20, 20);
		}
	}
}