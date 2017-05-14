package panel;

import UI.KeyManager;
import UI.MouseManager;
import display.Display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class Search extends Panel {

	private static final long serialVersionUID = 1L;
	MouseManager mouseManager;
	KeyManager keyManager;
	private JPanel searchPanel, naviPanel, infoPanel;
	private JTextField infoTextField, fromTextField, toTextField;
	private JTabbedPane tabbedPane;

	public Search(int x, int y, int width, int height, Display display, MouseManager mouseManager,
			KeyManager keyManager) {
		super(x, y, width, height, display);
		this.mouseManager = mouseManager;
		this.keyManager = keyManager;
		init();

	}

	public void init() {
		// searchPanel
		searchPanel = new JPanel(new BorderLayout());
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setPreferredSize(new Dimension(200, 50));
		searchPanel.setMaximumSize(new Dimension(200, 50));
		searchPanel.setMinimumSize(new Dimension(200, 50));

		// tabPanel
		tabbedPane = new JTabbedPane();
		infoTab();
		naviTab();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		searchPanel.add(tabbedPane);

		// important
		display.getPanel().add(BorderLayout.WEST, searchPanel);
		display.getFrame().setVisible(true);
	}

	public void infoTab() {
		infoPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);
		ImageIcon icon = createImageIcon("");
		infoTextField = new JTextField("Enter text here");
		infoTextField.setPreferredSize(new Dimension(190, 30));
		infoTextField.setMaximumSize(new Dimension(190, 30));
		infoTextField.setMinimumSize(new Dimension(190, 30));
		infoTextField.selectAll();
		infoTextField.addActionListener(new InfoListener());
		infoPanel.add(infoTextField);
		tabbedPane.addTab("Info", icon, infoPanel, "Station Infomation");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	}

	public void naviTab() {
		naviPanel = new JPanel();
		naviPanel.setBackground(Color.WHITE);
		ImageIcon icon = createImageIcon("");
		
		// from
		fromTextField = new JTextField("Enter text here");
		fromTextField.setPreferredSize(new Dimension(190, 30));
		fromTextField.setMaximumSize(new Dimension(190, 30));
		fromTextField.setMinimumSize(new Dimension(190, 30));
		fromTextField.selectAll();
		fromTextField.addActionListener(new FromListener());
		naviPanel.add(fromTextField);
		
		// to
		toTextField = new JTextField("Enter text here");
		toTextField.setPreferredSize(new Dimension(190, 30));
		toTextField.setMaximumSize(new Dimension(190, 30));
		toTextField.setMinimumSize(new Dimension(190, 30));
		toTextField.selectAll();
		naviPanel.add(toTextField);
		toTextField.addActionListener(new ToListener());
		
		tabbedPane.addTab("Navi", icon, naviPanel, "Navigation");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Search.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public class InfoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(infoTextField.getText());
		}
	}

	public class FromListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(fromTextField.getText());
		}
	}

	public class ToListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println(toTextField.getText());
		}
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
	}
}
