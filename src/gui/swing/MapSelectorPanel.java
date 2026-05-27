package gui.swing;

import gui.application.MapCatalog;
import gui.application.MapDescriptor;
import gui.application.GameSession;
import gui.application.GameSessionFactory;
import gui.layout.MapLayout;
import gui.snapshot.GameSnapshot;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

	private final JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
	private final JButton backButton = new JButton(SwingActionText.BACK_TO_MENU);
	private final GameSessionFactory previewSessionFactory = new GameSessionFactory();
	private final Map<String, PreviewData> previewDataByMapId = new LinkedHashMap<>();
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
		rebuildPreviewData();
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

	private void rebuildPreviewData() {
		previewDataByMapId.clear();
		if (catalog == null) {
			return;
		}

		for (MapDescriptor descriptor : catalog.getAllMaps()) {
			if (descriptor == null) {
				continue;
			}
			previewDataByMapId.put(descriptor.getId(), loadPreviewData(descriptor));
		}
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

		MapPreviewPanel preview = new MapPreviewPanel(resolvePreviewData(descriptor));
		preview.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		preview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		preview.setToolTipText("Pálya kiválasztása");
		preview.setSelectionAction(() -> fireSelection(descriptor));
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

	private PreviewData resolvePreviewData(MapDescriptor descriptor) {
		if (descriptor == null) {
			return PreviewData.unavailable("Előnézet nem érhető el.");
		}

		PreviewData data = previewDataByMapId.get(descriptor.getId());
		if (data != null) {
			return data;
		}

		PreviewData loaded = loadPreviewData(descriptor);
		previewDataByMapId.put(descriptor.getId(), loaded);
		return loaded;
	}

	private PreviewData loadPreviewData(MapDescriptor descriptor) {
		try {
			GameSession session = previewSessionFactory.createSession(descriptor);
			MapLayout layout = session.getMapLayout();
			GameSnapshot snapshot = session.getSnapshot();
			if (layout == null || snapshot == null) {
				return PreviewData.unavailable("Előnézet nem érhető el.");
			}

			return PreviewData.available(layout, toMapOnlySnapshot(snapshot));
		} catch (Exception exception) {
			return PreviewData.unavailable("Előnézet nem érhető el.");
		}
	}

	private GameSnapshot toMapOnlySnapshot(GameSnapshot snapshot) {
		List<GameSnapshot.Entry> filteredEntries = new ArrayList<>();
		for (GameSnapshot.Entry entry : snapshot.getEntries()) {
			if (entry == null) {
				continue;
			}

			String category = entry.getCategory();
			if ("node".equals(category) || "road".equals(category) || "lane".equals(category)) {
				filteredEntries.add(entry);
			}
		}

		return new GameSnapshot(snapshot.getTickCount(), snapshot.getCurrentPlayerId(),
			filteredEntries, Collections.emptyList());
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
		private final MapCanvas mapCanvas;
		private final JLabel fallbackLabel;

		MapPreviewPanel(PreviewData data) {
			setLayout(new BorderLayout());
			setBackground(new Color(241, 245, 249));
			setPreferredSize(new Dimension(150, 135));

			if (data != null && data.isAvailable()) {
				mapCanvas = new MapCanvas();
				mapCanvas.setOpaque(false);
				mapCanvas.setSelectionState(new SelectionState());
				mapCanvas.setLayout(data.layout);
				mapCanvas.setSnapshot(data.snapshot);
				fallbackLabel = null;
				add(mapCanvas, BorderLayout.CENTER);
			} else {
				mapCanvas = null;
				fallbackLabel = new JLabel(data == null ? "Előnézet" : data.message, SwingConstants.CENTER);
				fallbackLabel.setFont(fallbackLabel.getFont().deriveFont(Font.PLAIN, 11f));
				fallbackLabel.setForeground(new Color(71, 85, 105));
				add(fallbackLabel, BorderLayout.CENTER);
			}
		}

		void setSelectionAction(Runnable selectionAction) {
			if (selectionAction == null) {
				return;
			}
			MouseAdapter listener = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					selectionAction.run();
				}
			};
			addMouseListener(listener);
			if (mapCanvas != null) {
				mapCanvas.addMouseListener(listener);
			}
			if (fallbackLabel != null) {
				fallbackLabel.addMouseListener(listener);
			}
		}
	}

	private static final class PreviewData {
		private final MapLayout layout;
		private final GameSnapshot snapshot;
		private final String message;

		private PreviewData(MapLayout layout, GameSnapshot snapshot, String message) {
			this.layout = layout;
			this.snapshot = snapshot;
			this.message = message;
		}

		private static PreviewData available(MapLayout layout, GameSnapshot snapshot) {
			return new PreviewData(layout, snapshot, "");
		}

		private static PreviewData unavailable(String message) {
			return new PreviewData(null, null, message == null ? "Előnézet" : message);
		}

		private boolean isAvailable() {
			return layout != null && snapshot != null;
		}
	}
}