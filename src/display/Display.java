package display;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.HashMap;

import UI.KeyManager;
import UI.MouseManager;
import client.Client;
import init_data.Line;
import init_data.Station;
import panel.Map;
import panel.Search;

public class Display {

	private JFrame frame;
	private JPanel panel, canvasPanel;
	private Canvas canvas;

	private Map map;
	private Search search;

	private String title;
	private int width, height;

	private KeyManager keyManager;
	private MouseManager mouseManager;

	private Client client;
	private HashMap<String, Station> nameStations;
	private HashMap<Integer, Station> idStations;
	private HashMap<String, Line> idLines;

	public Display(String title, int width, int height, KeyManager keyManager, MouseManager mouseManager) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.keyManager = keyManager;
		this.mouseManager = mouseManager;

		init();
	}

	private void init() {
		// frame
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setPreferredSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);

		// canvas
		canvasPanel = new JPanel(new BorderLayout());
		canvasPanel.setPreferredSize(new Dimension(width * 4 / 5, height));
		canvasPanel.setMaximumSize(new Dimension(width * 4 / 5, height));
		canvasPanel.setMinimumSize(new Dimension(width * 4 / 5, height));
		canvas = new Canvas();
		canvas.setFocusable(false);
		canvasPanel.setBackground(Color.WHITE);
		canvasPanel.add(BorderLayout.CENTER, canvas);

		// panel
		panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(width, height));
		panel.setBackground(Color.WHITE);
		panel.add(BorderLayout.CENTER, canvasPanel);

		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		client = new Client();

		nameStations = new HashMap<String, Station>();
		idStations = new HashMap<Integer, Station>();
		idLines= new HashMap<String, Line>();
		client.handleRequestStations(client.handleRequest("http://server/stations/"), nameStations, idStations);
		client.handleRequestLines(client.handleRequest("http://server/lines/"), idLines);
		client.close();

		map = new Map(0, 0, width * 4 / 5, height, this, mouseManager);
		//search = new Search(0, 0, width / 5, height, this, mouseManager, keyManager);
		search = new Search(0, 0, 256, height, this, mouseManager, keyManager);

	}

	// Methods

	public void tick() {
		if (frame.getWidth() != width || frame.getHeight() != height) {
			width = frame.getWidth();
			height = frame.getHeight();
			//resize();
		}
		map.tick();
		search.tick();
	}

	public void render(Graphics g) {
		map.render(g);
		search.render(g);
	}

	public void resize() {
		canvasPanel.setPreferredSize(new Dimension(width * 4 / 5, height));
		canvasPanel.setMaximumSize(new Dimension(width * 4 / 5, height));
		canvasPanel.setMinimumSize(new Dimension(width * 4 / 5, height));

		search.resize(width / 5, height);
	}

	// GETTER AND SETTER

	public JPanel getPanel() {
		return panel;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}

	public HashMap<String, Station> getNameStations() {
		return nameStations;
	}

	public HashMap<Integer, Station> getIdStations() {
		return idStations;
	}

	public Map getMap() {
		return map;
	}

	public Search getSearch() {
		return search;
	}

	public HashMap<String, Line> getIdLines() {
		return idLines;
	}
	

}
