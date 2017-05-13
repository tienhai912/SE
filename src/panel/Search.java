package panel;

import UI.KeyManager;
import UI.MouseManager;
import display.Display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class Search extends Panel {

	private static final long serialVersionUID = 1L;
	MouseManager mouseManager;
	KeyManager keyManager;
	private JPanel searchPanel;
	private JTextField textField;

	public Search(int x, int y, int width, int height, Display display, MouseManager mouseManager,
			KeyManager keyManager) {
		super(x, y, width, height, display);
		this.mouseManager = mouseManager;
		this.keyManager = keyManager;

		// searchPanel
		searchPanel = new JPanel(new BorderLayout());
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setPreferredSize(new Dimension(200, 50));
		searchPanel.setMaximumSize(new Dimension(200, 50));
		searchPanel.setMinimumSize(new Dimension(200, 50));
		searchPanel.setOpaque(true);
		searchPanel.setBounds(0, 0, 200, 50);
		// textField
		textField = new JTextField("Enter text here");
		textField.setPreferredSize(new Dimension(200, 50));
		textField.setMaximumSize(new Dimension(200, 50));
		textField.setMinimumSize(new Dimension(200, height));
		textField.selectAll();
		searchPanel.add(BorderLayout.NORTH, textField);

		// important
		display.getPanel().add(BorderLayout.WEST, searchPanel);
		display.getFrame().setVisible(true);
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
	}
}
