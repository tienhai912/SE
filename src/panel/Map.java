package panel;

import UI.MouseManager;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import display.Display;
import display.ImageLoader;
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
		} else if (frameWidth - size[sizeNum] - 200 < 0) {
			if (x < frameWidth - size[sizeNum] - 200) {
				x = frameWidth - size[sizeNum] - 200;
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
	public void clickedStation() {
		Station clickedStation = checkClickedStation();
		if (clickedStation == null) {

		} else {
			if (display.getSearch().getSelectedTab() == 0) {
				display.getSearch().setInfoStation(clickedStation);
				display.getSearch().setInfoText(clickedStation.getName());
				display.getSearch().infoShowResult(clickedStation.getName(), clickedStation.getId());
			}
		}
	}

	public Station checkClickedStation() {
		if (mouseManager.isClick()) {
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
			mouseManager.setClick(false);

			return clickedStation;
		}
		return null;
	}

	public void drawClickStation(Graphics g) {
		Station station = display.getSearch().getInfoStation();
		if (display.getSearch().getSelectedTab() == 0 && station != null) {
			if (station.isRectangle()) {
				MapPoint p0, p1;
				p0 = station.getCoordinates().get(0);
				p1 = station.getCoordinates().get(1);
				g.fillRect((int) currentSizePosition(p0.getX()) + x, (int) currentSizePosition(p0.getY()) + y,
						(int) currentSizePosition(p1.getX() - p0.getX()),
						(int) currentSizePosition(p1.getY() - p0.getY()));

			}

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
		clickedStation();

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(map, x, y, size[sizeNum], size[sizeNum], null);
		drawClickStation(g);
	}
}
