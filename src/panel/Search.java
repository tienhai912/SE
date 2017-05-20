package panel;

import UI.KeyManager;
import UI.MouseManager;
import display.Display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
	
	// init

	public void init() {
		// searchPanel
		searchPanel = new JPanel(new BorderLayout());
		searchPanel.setBackground(Color.WHITE);
		searchPanel.setPreferredSize(new Dimension(width, height));

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
		infoTextField.setPreferredSize(new Dimension(width - 10, 30));
		infoTextField.selectAll();

		// listener
		infoTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				infoTextFieldMouseClicked(evt);
			}
		});

		infoTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				infoTextFieldKeyPressed(evt);
			}
		});
		//
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
		fromTextField.setPreferredSize(new Dimension(width - 10, 30));
		fromTextField.selectAll();
		naviPanel.add(fromTextField);

		// listener
		fromTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				fromTextFieldMouseClicked(evt);
			}
		});
		fromTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				fromTextFieldKeyPressed(evt);
			}
		});

		// to
		toTextField = new JTextField("Enter text here");
		toTextField.setPreferredSize(new Dimension(width - 10, 30));
		toTextField.selectAll();
		naviPanel.add(toTextField);

		// listener
		toTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				toTextFieldMouseClicked(evt);
			}
		});
		toTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				toTextFieldKeyPressed(evt);
			}
		});

		//
		tabbedPane.addTab("Navi", icon, naviPanel, "Navigation");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	}
	
	//methods
	
	public void resize(int width, int height){
		searchPanel.setPreferredSize(new Dimension(width, height));
		searchPanel.setMaximumSize(new Dimension(width, height));
		searchPanel.setMinimumSize(new Dimension(width, height));
		infoTextField.setPreferredSize(new Dimension(width - 10, 30));
		fromTextField.setPreferredSize(new Dimension(width - 10, 30));
		toTextField.setPreferredSize(new Dimension(width - 10, 30));
		
	}

	// Listener methods
	private void infoTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		infoTextField.setText("");
	}

	private void fromTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		fromTextField.setText("");
	}

	private void toTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		toTextField.setText("");
	}

	private void infoTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println(infoTextField.getText());
			infoTextField.setText("");
		}
	}

	private void fromTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println(fromTextField.getText());
			fromTextField.setText("");
		}
	}

	private void toTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println(toTextField.getText());
			toTextField.setText("");
		}
	}
	
	//

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Search.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
	}
}
