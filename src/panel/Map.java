package panel;

import UI.MouseManager;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import display.Display;
import display.ImageLoader;
import init_data.Line;
import init_data.MapPoint;
import init_data.Station;

import java.awt.Graphics;

public class Map extends Panel {

	private static final long serialVersionUID = 1L;
	private int size[];
	private int preX, preY, frameWidth, frameHeight;
	MouseManager mouseManager;
	private int mapNum, sizeNum;
	private String mapPath[];
	private BufferedImage map;

	public Map(int x, int y, int width, int height, Display display, MouseManager mouseManager) {
		super(x, y, width, height, display);
		preX = 0;
		preY = 0;
		this.mouseManager = mouseManager;

		// size array
		sizeNum = 0;
		size = new int[4];
		size[0] = 1000;
		size[1] = 2000;
		size[2] = 3000;
		size[3] = 4000;

		// map array
		mapNum = 0;
		mapPath = new String[2];
		mapPath[0] = "/2000x2000.png";
		mapPath[1] = "/4000x4000.png";

		//
		map = ImageLoader.loadImage(mapPath[mapNum]);
	}

	public void dragMap() {
		updateX();
		updateY();
		if (mouseManager.isPressed())
			rescale();
	}

	public void updateX() {
		if (mouseManager.isPressed()) {
			if (preX == 0) {
				preX = mouseManager.getX();
			} else {
				x += mouseManager.getX() - preX;
				preX = mouseManager.getX();
			}
		} else {
			preX = 0;
		}
	}

	public void updateY() {
		if (mouseManager.isPressed()) {
			if (preY == 0) {
				preY = mouseManager.getY();
			} else {
				y += mouseManager.getY() - preY;
				preY = mouseManager.getY();
			}
		} else {
			preY = 0;
		}
	}

	public void zoom() {
		if (mouseManager.isZoomIn()) {
			if (sizeNum < size.length - 1) {
				sizeNum++;
				mapNum = sizeNum / 2;
				map = ImageLoader.loadImage(mapPath[mapNum]);
				focusZoom(size[sizeNum - 1], size[sizeNum]);
				rescale();

			}
			mouseManager.setZoom();

		} else if (mouseManager.isZoomOut()) {
			if (sizeNum > 0) {
				sizeNum--;
				mapNum = sizeNum / 2;
				map = ImageLoader.loadImage(mapPath[mapNum]);
				focusZoom(size[sizeNum + 1], size[sizeNum]);
				rescale();

			}
			mouseManager.setZoom();
		}

	}

	public void focusZoom(int sizeBefore, int sizeAfter) {
		int tempX, tempY;
		tempX = mouseManager.getX();
		tempY = mouseManager.getY();
		x = (int) ((x - tempX) * ((float) sizeAfter / (float) sizeBefore) + tempX);
		y = (int) ((y - tempY) * ((float) sizeAfter / (float) sizeBefore) + tempY);

	}

	public void rescale() {
		if (x > 0) {
			x = 0;
		} else if (frameWidth - size[sizeNum] - 276 < 0) {
			if (x < frameWidth - size[sizeNum] - 276) {
				x = frameWidth - size[sizeNum] - 276;
			}
		} else {
			x = 0;
		}
		if (y > 0) {
			y = 0;
		} else if (frameWidth - size[sizeNum] - 50 < 0) {
			if (y < frameHeight - size[sizeNum] - 50) {
				y = frameHeight - size[sizeNum] - 50;
			}
		} else {
			y = 0;
		}
	}

	/// map click methods
	public void mapClicked() {
		if (mouseManager.isClick()) {
			Station clickedStation = checkClickedStation();
			if (clickedStation == null) {
				if (display.getSearch().getSelectedTab() == 0) {
					Line clickedLine = checkClickedLine();
					if (clickedLine != null) {
						display.getSearch().setInfoLine(clickedLine);
						display.getSearch().setInfoText(clickedLine.getCode());
						display.getSearch().infoShowLineResult();

						display.getSearch().setInfoStation(null);
					}
				}

			} else {
				if (display.getSearch().getSelectedTab() == 0) {
					display.getSearch().setInfoStation(clickedStation);
					display.getSearch().setInfoText(clickedStation.getName());
					display.getSearch().infoShowStationResult();

					display.getSearch().setInfoLine(null);
				} else {
					if (display.getSearch().isFromCursor()) {
						display.getSearch().setFromStation(clickedStation);
						display.getSearch().setFromText(clickedStation.getName());
						display.getSearch().setFromCursor(false);
						display.getSearch().naviShowResult();
					} else {
						display.getSearch().setToStation(clickedStation);
						display.getSearch().setToText(clickedStation.getName());
						display.getSearch().setFromCursor(true);
						display.getSearch().naviShowResult();
					}
				}
			}
			mouseManager.setClick(false);
		}
	}

	public Station checkClickedStation() {
		Station clickedStation = null;
		float x, y;
		x = defaultSizePosition((float) (mouseManager.getX() - this.x));
		y = defaultSizePosition((float) (mouseManager.getY() - this.y));
		for (HashMap.Entry<Integer, Station> entry : display.getIdStations().entrySet()) {
			Station temp = entry.getValue();
			if (temp.isInside(x, y)) {
				clickedStation = temp;
				break;
			}
		}

		return clickedStation;
	}

	public Line checkClickedLine() {
		Line clickedLine = null;
		float x, y;
		x = defaultSizePosition((float) (mouseManager.getX() - this.x));
		y = defaultSizePosition((float) (mouseManager.getY() - this.y));
		for (HashMap.Entry<String, Line> entry : display.getIdLines().entrySet()) {
			Line temp = entry.getValue();
			if (temp.isInside(x, y)) {
				clickedLine = temp;
				break;
			}
		}
		mouseManager.setClick(false);

		return clickedLine;

	}

	public void drawResult(Graphics g) {

		if (display.getSearch().getSelectedTab() == 0) {
			Station station = display.getSearch().getInfoStation();
			Line line = display.getSearch().getInfoLine();
			if (station != null) {
				drawStation(g, station);
			} else if (line != null) {
				for (int temp : display.getSearch().getInfoLineStationsId()) {
					Station tempStation = display.getIdStations().get(temp);
					drawStation(g,tempStation);
				}
			}

		} else if (display.getSearch().getSelectedTab() == 1 && display.getSearch().getFromStation() != null
				&& display.getSearch().getToStation() != null) {
			for (Station temp : display.getSearch().getNaviStations()) {
				drawStation(g, temp);
			}
		}
	}

	public void drawStation(Graphics g, Station station) {
		if (station.isRectangle()) {
			MapPoint p0, p1;
			p0 = station.getCoordinates().get(0);
			p1 = station.getCoordinates().get(1);
			g.fillRect((int) currentSizePosition(p0.getX() - 2) + x, (int) currentSizePosition(p0.getY() - 2) + y,
					(int) currentSizePosition(p1.getX() - p0.getX() + 8),
					(int) currentSizePosition(p1.getY() - p0.getY() + 8));
			/*
			 * Graphics2D g2 = (Graphics2D) g; float dash1[] = { 1f };
			 * BasicStroke dashed = new BasicStroke(10.0f,
			 * BasicStroke.JOIN_MITER, BasicStroke.JOIN_MITER, 10.0f, dash1,
			 * 0.0f); BasicStroke dashed2 = new BasicStroke(10f);
			 * 
			 * g2.setStroke(dashed2); g2.drawLine((int)
			 * currentSizePosition(p0.getX()) + x, (int)
			 * currentSizePosition(p0.getY()) + y, (int)
			 * currentSizePosition(p1.getX()) + x, (int)
			 * currentSizePosition(p1.getY()) + y);
			 */
		}
	}

	public float currentSizePosition(float x) {
		return x * ((float) size[sizeNum] / 4000f);
	}

	public float defaultSizePosition(float x) {
		return x * (4000f / (float) size[sizeNum]);
	}

	public int currentSizePosition(int x) {
		return (int) ((float) x * ((float) size[sizeNum] / 4000f));
	}

	@Override
	public void tick() {
		frameWidth = (int) display.getFrame().getBounds().getWidth();
		frameHeight = (int) display.getFrame().getBounds().getHeight();
		dragMap();
		zoom();
		mapClicked();

	}

	@Override
	public void render(Graphics g) {

		g.drawImage(map, x, y, size[sizeNum], size[sizeNum], null);
		drawResult(g);
	}
}
