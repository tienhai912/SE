package panel;

import UI.KeyManager;
import UI.MouseManager;
import client.Client;
import display.Display;
import init_data.Line;
import init_data.Station;
import suggestion.suggestionBox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionListener;

public class Search extends Panel {

	private static final long serialVersionUID = 1L;
	MouseManager mouseManager;
	KeyManager keyManager;
	private JPanel searchPanel, naviPanel, infoPanel, infoResultPanel, naviResultPanel;
	private suggestionBox infoTextField, fromTextField, toTextField;
	private JTabbedPane tabbedPane;

	private JTextPane infoResultTextPane, naviResultTextPane;
	private StyledDocument infoDoc, naviDoc;
	private Font infoFont;
	private Station infoStation, fromStation, toStation;
	private Line infoLine;
	private ArrayList<String> suggestionStrings;
	private ArrayList<Station> naviStations;
	private ArrayList<Integer> naviStationsId, infoLineStationsId;
	private boolean shortestRoad, fromCursor;

	private int count = 0;

	public Search(int x, int y, int width, int height, Display display, MouseManager mouseManager,
			KeyManager keyManager) {
		super(x, y, width, height, display);
		this.mouseManager = mouseManager;
		this.keyManager = keyManager;
		init();
		infoTextField.requestFocusInWindow();
	}

	// init

	public void init() {
		// suggestion
		suggestionStrings = new ArrayList<String>();
		for (HashMap.Entry<Integer, Station> entry : display.getIdStations().entrySet()) {
			Station temp = entry.getValue();
			suggestionStrings.add(temp.getName());
		}
		for (HashMap.Entry<String, Line> entry : display.getIdLines().entrySet()) {
			Line temp = entry.getValue();
			suggestionStrings.add(temp.getCode()+": " +temp.getName());
		}
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

	///////////////////////////////////////////////////////////////////////// infoTab

	public void infoTab() {
		infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
		infoPanel.setBackground(Color.WHITE);
		ImageIcon icon = createImageIcon("/train.png");

		// infoTextField
		createInfoTextField();

		// infoResultPanel
		createInfoResultPanel();

		//
		tabbedPane.addTab("Infomation", icon, infoPanel, "System Infomation");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
	}

	public void createInfoTextField() {

		// name
		infoPanel.add(Box.createHorizontalStrut(20));
		JLabel infoText = new JLabel("Station");
		infoPanel.add(infoText);
		infoPanel.add(Box.createHorizontalStrut(20));

		// infoTextField
		infoTextField = new suggestionBox(suggestionStrings);
		infoTextField.setPreferredSize(new Dimension(width - 40, 30));
		infoTextField.selectAll();
		infoPanel.add(infoTextField);

		// delete button
		ImageIcon deleteIcon = createImageIcon("/cancel.png");
		JButton infoDeleteButton = new JButton();
		infoDeleteButton.setIcon(deleteIcon);
		infoDeleteButton.setPreferredSize(new Dimension(30, 30));
		infoDeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				infoTextField.setText("");
				infoTextField.requestFocusInWindow();
			}
		});

		infoPanel.add(infoDeleteButton);

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
	}

	public void createInfoResultPanel() {
		// infoResultPanel
		infoResultPanel = new JPanel();
		infoResultPanel.setPreferredSize(new Dimension(width, height - 50));
		infoResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		infoResultPanel.setBackground(Color.WHITE);

		infoResultTextPane = new JTextPane();
		infoDoc = infoResultTextPane.getStyledDocument();

		infoResultPanel.add(infoResultTextPane);
		infoPanel.add(infoResultPanel);
	}

	/////////////////////////////////////////////////////////////////////////// naviTab

	public void naviTab() {
		naviPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
		naviPanel.setBackground(Color.WHITE);
		ImageIcon icon = createImageIcon("/arrow.png");

		// fromTextField
		createFromTextField();

		// toTextField
		createToTextField();

		// swapButton

		createSwapButton();

		// choiceBox
		createChoiceBox();

		// naviResultPanel
		createNaviResultPanel();

		//
		tabbedPane.addTab("Navigation", icon, naviPanel, "Navigation");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		fromCursor = true;
	}

	public void createFromTextField() {
		// from
		naviPanel.add(Box.createHorizontalStrut(20));
		JLabel fromText = new JLabel("Starting Point");
		naviPanel.add(fromText);
		naviPanel.add(Box.createHorizontalStrut(20));
		fromTextField = new suggestionBox(suggestionStrings);
		fromTextField.setPreferredSize(new Dimension(width - 40, 30));
		fromTextField.selectAll();

		naviPanel.add(fromTextField);

		// delete button
		ImageIcon deleteIcon = createImageIcon("/cancel.png");
		JButton fromDeleteButton = new JButton();
		fromDeleteButton.setIcon(deleteIcon);
		fromDeleteButton.setPreferredSize(new Dimension(30, 30));
		fromDeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				fromTextField.setText("");
				fromTextField.requestFocusInWindow();
				fromCursor = true;
			}
		});

		naviPanel.add(fromDeleteButton);

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
	}

	public void createToTextField() {
		// to
		naviPanel.add(Box.createHorizontalStrut(20));
		JLabel toText = new JLabel("Destination");
		naviPanel.add(toText);
		naviPanel.add(Box.createHorizontalStrut(20));
		toTextField = new suggestionBox(suggestionStrings);
		toTextField.setPreferredSize(new Dimension(width - 40, 30));
		toTextField.selectAll();
		naviPanel.add(toTextField);

		// delete button
		ImageIcon deleteIcon = createImageIcon("/cancel.png");
		JButton toDeleteButton = new JButton();
		toDeleteButton.setIcon(deleteIcon);
		toDeleteButton.setPreferredSize(new Dimension(30, 30));
		toDeleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				toTextField.setText("");
				toTextField.requestFocusInWindow();
				fromCursor = false;
			}
		});

		naviPanel.add(toDeleteButton);

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
	}

	public void createSwapButton() {
		// swapButton

		naviPanel.add(Box.createHorizontalStrut(20));
		ImageIcon swapIcon = createImageIcon("/switch.png");
		JButton swapButton = new JButton("Swap");
		swapButton.setIcon(swapIcon);
		swapButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				swapButtonMouseClicked(evt);
			}
		});
		naviPanel.add(swapButton);
		naviPanel.add(Box.createHorizontalStrut(20));
	}

	public void createChoiceBox() {
		// choiceBox
		String list[] = { "Shortest Road", "Least train Switching" };
		JComboBox<String> choiceBox = new JComboBox<String>(list);
		choiceBox.setEditable(false);
		choiceBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentString = (String) choiceBox.getSelectedItem();
				if (currentString.equals("Shortest Road"))
					shortestRoad = true;
				else
					shortestRoad = false;
				naviShowResult();
			}
		});
		shortestRoad = true;
		naviPanel.add(choiceBox);

	}

	public void createNaviResultPanel() {
		// infoResultPanel
		naviResultPanel = new JPanel();
		naviResultPanel.setPreferredSize(new Dimension(width, height - 50));
		naviResultPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		naviResultPanel.setBackground(Color.WHITE);

		naviResultTextPane = new JTextPane();
		naviDoc = naviResultTextPane.getStyledDocument();

		naviResultPanel.add(naviResultTextPane);
		naviPanel.add(naviResultPanel);
	}

	// methods

	public void resize(int width, int height) {
		searchPanel.setPreferredSize(new Dimension(width, height));
		searchPanel.setMaximumSize(new Dimension(width, height));
		searchPanel.setMinimumSize(new Dimension(width, height));
		infoTextField.setPreferredSize(new Dimension(width - 40, 30));
		fromTextField.setPreferredSize(new Dimension(width - 40, 30));
		toTextField.setPreferredSize(new Dimension(width - 40, 30));
		infoResultPanel.setPreferredSize(new Dimension(width, height));
		naviResultPanel.setPreferredSize(new Dimension(width, height));
		searchPanel.updateUI();

	}

	public int getStationId(String stationName) {
		int stationId = -1;
		if (!display.getNameStations().containsKey(stationName))
			return -1;
		stationId = ((Station) display.getNameStations().get(stationName)).getId();
		return stationId;
	}

	public Line getLine(String code) {
		if (!display.getIdLines().containsKey(code)) {
			return null;
		}
		return display.getIdLines().get(code);
	}

	public void infoShowStationResult() {
		String result, resultArray[];
		// handle
		Client client = new Client();
		result = client.handleRequestArrivalTime(
				client.handleRequest("http://localhost/stations/" + infoStation.getId() + "/arrivaltimeinfo"));
		result.trim();
		resultArray = result.split("\\r\\n|\\n|\\r");

		// output
		infoResultPanel.removeAll();
		infoResultTextPane = new JTextPane();
		infoDoc = infoResultTextPane.getStyledDocument();
		try {
			mainStationWordStyle(infoDoc);
			infoDoc.insertString(infoDoc.getLength(), infoStation.getName() + "\n", null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < resultArray.length; i++) {
			if (i % 4 == 0) {
				try {
					stationWordStyle(infoDoc);
					infoDoc.insertString(infoDoc.getLength(), "\n" + resultArray[i] + "\n", null);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			else {
				try {
					infoWordStyle(infoDoc);
					infoDoc.insertString(infoDoc.getLength(), resultArray[i] + "\n", null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		infoResultPanel.add(infoResultTextPane);
		infoResultPanel.updateUI();
	}

	public void infoShowLineResult() {
		String result, resultArray[];
		// handle

		infoLineStationsId = new ArrayList<Integer>();
		Client client = new Client();
		result = client.handleRequestLineId(client.handleRequest("http://localhost/line/" + infoLine.getId()),
				infoLineStationsId);
		result.trim();
		resultArray = result.split("\\r\\n|\\n|\\r");

		// output
		infoResultPanel.removeAll();
		infoResultTextPane = new JTextPane();
		infoDoc = infoResultTextPane.getStyledDocument();

		for (int i = 0; i < resultArray.length; i++) {
			try {
				if (i == 0) {
					mainStationWordStyle(infoDoc);
					infoDoc.insertString(infoDoc.getLength(), "Line " + resultArray[i] + "\n", null);
				} else if (i == 1) {
					stationWordStyle(infoDoc);
					infoDoc.insertString(infoDoc.getLength(), "\n" + resultArray[i] + "\n", null);
				} else {
					infoWordStyle(infoDoc);
					infoDoc.insertString(infoDoc.getLength(), "\n" + resultArray[i], null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		infoResultPanel.add(infoResultTextPane);
		infoResultPanel.updateUI();
	}

	public void naviShowResult() {
		if (fromStation != null && toStation != null) {
			String result, resultArray[];
			// handle
			Client client = new Client();
			naviStationsId = new ArrayList<Integer>();
			if (shortestRoad) {
				result = client.handleRequestNavi(
						client.handleRequest(
								"http://localhost/nav/" + fromStation.getId() + "/" + toStation.getId() + "/shortest"),
						naviStationsId);
			} else {
				result = client.handleRequestNavi(client.handleRequest(
						"http://localhost/nav/" + fromStation.getId() + "/" + toStation.getId() + "/leasttransfer"),
						naviStationsId);
			}
			result.trim();
			resultArray = result.split("\\r\\n|\\n|\\r");
			naviStations = new ArrayList<Station>();
			for (int i : naviStationsId) {
				naviStations.add((Station) display.getIdStations().get(i));
			}

			// output
			naviResultPanel.removeAll();
			naviResultTextPane = new JTextPane();
			naviDoc = naviResultTextPane.getStyledDocument();
			try {
				mainStationWordStyle(naviDoc);
				naviDoc.insertString(naviDoc.getLength(), fromStation.getName() + "\n", null);
				stationWordStyle(naviDoc);
				naviDoc.insertString(naviDoc.getLength(), "to\n", null);
				mainStationWordStyle(naviDoc);
				naviDoc.insertString(naviDoc.getLength(), toStation.getName() + "\n", null);
				stationWordStyle(naviDoc);
				naviDoc.insertString(naviDoc.getLength(), resultArray[0] + " km\n", null);
				naviDoc.insertString(naviDoc.getLength(), resultArray[1] + " $\n", null);

			} catch (Exception e) {
				e.printStackTrace();
			}

			naviResultPanel.add(naviResultTextPane);
			naviResultPanel.updateUI();
		}

	}

	public int getSelectedTab() {
		return tabbedPane.getSelectedIndex();
	}

	public void setInfoText(String s) {
		infoTextField.setText("");
		infoTextField.setNeedSuggestion(false);
		infoTextField.setText(s);
		infoTextField.requestFocusInWindow();
		infoTextField.setCaretPosition(infoTextField.getText().length());
	}

	public void setFromText(String s) {
		fromTextField.setText("");
		fromTextField.setNeedSuggestion(false);
		fromTextField.setText(s);
		fromTextField.requestFocusInWindow();
		fromTextField.setCaretPosition(fromTextField.getText().length());
	}

	public void setToText(String s) {
		toTextField.setText("");
		toTextField.setNeedSuggestion(false);
		toTextField.setText(s);
		toTextField.requestFocusInWindow();
		toTextField.setCaretPosition(toTextField.getText().length());
	}
	
	public String lineSlit(String s) {
		String time = s;
		String[] token = time.split(":");
		return token[0];
	}

	// Listener methods
	private void infoTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
	}

	private void fromTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		fromCursor = true;
	}

	private void toTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		fromCursor = false;
	}

	private void swapButtonMouseClicked(java.awt.event.MouseEvent evt) {
		String temp = fromTextField.getText();
		fromTextField.setText(toTextField.getText());
		toTextField.setText(temp);

	}

	private void infoTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			String str = infoTextField.getText();
			int stationId = getStationId(str);
			if (stationId == -1) {
				infoLine = getLine(lineSlit(str));
				if (infoLine != null) {
					infoShowLineResult();
					infoStation = null;
				}
			} else {
				infoStation = (Station) display.getNameStations().get(str);
				infoShowStationResult();
				infoLine = null;
			}

		}
	}

	private void fromTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			String stationName = fromTextField.getText();
			int stationId = getStationId(stationName);
			if (stationId == -1) {
				System.out.println("Problem from");
			} else {
				fromStation = (Station) display.getNameStations().get(stationName);
				naviShowResult();
			}
		}
	}

	private void toTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			String stationName = toTextField.getText();
			int stationId = getStationId(stationName);
			if (stationId == -1) {
				System.out.println("Problem to");
			} else {
				toStation = (Station) display.getNameStations().get(stationName);
				naviShowResult();
			}
		}
	}

	// icon

	protected ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Search.class.getResource(path);
		if (imgURL != null) {
			ImageIcon icon = new ImageIcon(imgURL);
			Image img = icon.getImage();
			Image newimg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(newimg);
			return icon;
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	// word style

	public void createInfoFont() {
		infoFont = infoTextField.getFont();
	}

	public void mainStationWordStyle(StyledDocument doc) {
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(aSet, infoFont.getSize() + 8);
		StyleConstants.setBold(aSet, true);
		StyleConstants.setForeground(aSet, Color.RED);
		doc.setParagraphAttributes(doc.getLength(), 0, aSet, false);

	}

	public void stationWordStyle(StyledDocument doc) {
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(aSet, infoFont.getSize() + 4);
		StyleConstants.setBold(aSet, true);
		StyleConstants.setForeground(aSet, Color.BLUE);
		doc.setParagraphAttributes(doc.getLength(), 0, aSet, false);

	}

	public void infoWordStyle(StyledDocument doc) {
		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setFontSize(aSet, infoFont.getSize());
		StyleConstants.setForeground(aSet, Color.BLACK);
		StyleConstants.setBold(aSet, false);
		doc.setParagraphAttributes(doc.getLength(), 0, aSet, false);
	}

	@Override
	public void tick() {
		count++;
		if (count >= 1800 && infoStation != null) {
			infoShowStationResult();

			count = 0;
		}
	}

	@Override
	public void render(Graphics g) {
	}

	// SETTER GETTER
	public Station getInfoStation() {
		return infoStation;
	}

	public void setInfoStation(Station infoStation) {
		this.infoStation = infoStation;
	}

	public Station getFromStation() {
		return fromStation;
	}

	public void setFromStation(Station fromStation) {
		this.fromStation = fromStation;
	}

	public Station getToStation() {
		return toStation;
	}

	public void setToStation(Station toStation) {
		this.toStation = toStation;
	}

	public boolean isFromCursor() {
		return fromCursor;
	}

	public void setFromCursor(boolean fromCursor) {
		this.fromCursor = fromCursor;
	}

	public boolean isShortestRoad() {
		return shortestRoad;
	}

	public ArrayList<Station> getNaviStations() {
		return naviStations;
	}

	public Line getInfoLine() {
		return infoLine;
	}

	public void setInfoLine(Line infoLine) {
		this.infoLine = infoLine;
	}

	public ArrayList<Integer> getInfoLineStationsId() {
		return infoLineStationsId;
	}

	/*
	 * 
	 * JPanel panel = new JPanel(); //remove all components in panel.
	 * panel.removeAll(); // refresh the panel. panel.updateUI(); // refresh the
	 * panel.updateUI();
	 * 
	 * 
	 * you can split a string by line break by using the following statement :
	 * 
	 * String textStr[] = yourString.split("\\r\\n|\\n|\\r");
	 * 
	 */

}
