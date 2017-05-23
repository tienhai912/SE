package panel;

import UI.KeyManager;
import UI.MouseManager;
import client.Client;
import display.Display;
import init_data.Station;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Search extends Panel {

	private static final long serialVersionUID = 1L;
	MouseManager mouseManager;
	KeyManager keyManager;
	private JPanel searchPanel, naviPanel, infoPanel, infoResultPanel;
	private JTextField infoTextField, fromTextField, toTextField;
	private JTabbedPane tabbedPane;

	private JTextPane infoResultTextPane;
	private StyledDocument doc;
	private Font infoFont;

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
		createInfoFont();
	}

	public void infoTab() {
		infoPanel = new JPanel();
		infoPanel.setBackground(Color.WHITE);
		ImageIcon icon = createImageIcon("");

		JLabel infoText = new JLabel("Station");
		infoPanel.add(infoText);
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
		// infoResultPanel
		infoResultPanel = new JPanel();
		infoResultPanel.setPreferredSize(new Dimension(width, height - 50));
		infoResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		infoResultPanel.setBackground(Color.WHITE);

		infoResultTextPane = new JTextPane();
		doc = infoResultTextPane.getStyledDocument();

		infoResultPanel.add(infoResultTextPane);

		//
		infoPanel.add(infoTextField);
		infoPanel.add(infoResultPanel);
		tabbedPane.addTab("Info", icon, infoPanel, "Station Infomation");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	}

	public void naviTab() {
		naviPanel = new JPanel();
		naviPanel.setBackground(Color.WHITE);
		ImageIcon icon = createImageIcon("");

		// from
		JLabel fromText = new JLabel("Starting Point");
		naviPanel.add(fromText);
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
		JLabel toText = new JLabel("Destination");
		naviPanel.add(toText);
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

		// swap
		JButton swapButton = new JButton("Swap");
		swapButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				swapButtonMouseClicked(evt);
			}
		});
		naviPanel.add(swapButton);

		//
		tabbedPane.addTab("Navi", icon, naviPanel, "Navigation");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
	}

	// methods

	public void resize(int width, int height) {
		searchPanel.setPreferredSize(new Dimension(width, height));
		searchPanel.setMaximumSize(new Dimension(width, height));
		searchPanel.setMinimumSize(new Dimension(width, height));
		infoTextField.setPreferredSize(new Dimension(width - 10, 30));
		fromTextField.setPreferredSize(new Dimension(width - 10, 30));
		toTextField.setPreferredSize(new Dimension(width - 10, 30));
		infoResultPanel.setPreferredSize(new Dimension(width, height));

	}

	public int getStationId(String stationName) {
		int stationId = -1;
		if (!display.getStations().containsKey(stationName))
			return -1;
		Set<Entry<String, Station>> set = display.getStations().entrySet();
		Iterator<Entry<String, Station>> it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Station> me = (Map.Entry<String, Station>) it.next();
			if (((String) me.getKey()).equals(stationName)) {
				stationId = ((Station) me.getValue()).getId();
				break;
			}
		}
		return stationId;
	}

	// Listener methods
	private void infoTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		if (infoTextField.getText().equals("Enter text here")) {
			infoTextField.setText("");
		}
	}

	private void fromTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		if (fromTextField.getText().equals("Enter text here")) {
			fromTextField.setText("");
		}
	}

	private void toTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		if (toTextField.getText().equals("Enter text here")) {
			toTextField.setText("");
		}
	}

	private void swapButtonMouseClicked(java.awt.event.MouseEvent evt) {
		String temp = fromTextField.getText();
		fromTextField.setText(toTextField.getText());
		toTextField.setText(temp);

	}

	private void infoTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			String stationName = infoTextField.getText(), result, resultArray[];
			int stationId = getStationId(stationName);
			if (stationId == -1) {
				System.out.println("Problem");
			} else {
				// handle
				Client client = new Client();
				result = client.handleRequestArrivalTime(
						client.handleRequest("http://localhost/stations/" + stationId + "/arrivaltimeinfo"));
				result.trim();
				resultArray = result.split("\\r\\n|\\n|\\r");

				// ouput
				infoResultPanel.removeAll();
				infoResultTextPane = new JTextPane();
				doc = infoResultTextPane.getStyledDocument();
				try {
					mainStationWordStyle();
					doc.insertString(doc.getLength(), stationName + "\n", null);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (int i = 0; i < resultArray.length; i++) {
					if (i % 4 == 0) {
						try {
							stationWordStyle();
							doc.insertString(doc.getLength(), "\n" + resultArray[i] + "\n", null);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					else {
						try {
							infoWordStyle();
							doc.insertString(doc.getLength(), resultArray[i] + "\n", null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				infoResultPanel.add(infoResultTextPane);
				infoResultPanel.updateUI();

			}

		}
	}

	private void fromTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println(fromTextField.getText());
		}
	}

	private void toTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			System.out.println(toTextField.getText());
		}
	}

	// icon

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Search.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	// word style

	public void createInfoFont() {
		infoFont = infoTextField.getFont();
	}

	public void mainStationWordStyle() {
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(aSet, infoFont.getSize() + 8);
		StyleConstants.setBold(aSet, true);
		StyleConstants.setForeground(aSet, Color.RED);
		doc.setParagraphAttributes(doc.getLength(), 0, aSet, false);

	}

	public void stationWordStyle() {
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(aSet, infoFont.getSize() + 4);
		StyleConstants.setBold(aSet, true);
		StyleConstants.setForeground(aSet, Color.BLUE);
		doc.setParagraphAttributes(doc.getLength(), 0, aSet, false);

	}

	public void infoWordStyle() {
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(aSet, infoFont.getSize());
		StyleConstants.setForeground(aSet, Color.BLACK);
		StyleConstants.setBold(aSet, false);
		doc.setParagraphAttributes(doc.getLength(), 0, aSet, false);
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
	}

	/*
	 * 
	 * JPanel panel = new JPanel(); //remove all components in panel.
	 * panel.removeAll(); // refresh the panel. panel.updateUI(); // refresh the
	 * panel. panel.updateUI();
	 * 
	 * 
	 * you can split a string by line break by using the following statement :
	 * 
	 * String textStr[] = yourString.split("\\r\\n|\\n|\\r");
	 * 
	 */
}
