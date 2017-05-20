package panel;

import UI.MouseManager;
import java.awt.image.BufferedImage;

import display.Display;
import display.ImageLoader;
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
		x = (int)((x - tempX) * ((float)sizeAfter / (float)sizeBefore) + tempX);
		y = (int)((y - tempY) * ((float)sizeAfter / (float)sizeBefore) + tempY);

	}

	public void rescale() {
		if (x > 0) {
			x = 0;
		} else if (frameWidth - size[sizeNum] - 220 < 0) {
			if (x < frameWidth - size[sizeNum] - 220) {
				x = frameWidth - size[sizeNum] - 220;
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

	@Override
	public void tick() {
		frameWidth = (int) display.getFrame().getBounds().getWidth();
		frameHeight = (int) display.getFrame().getBounds().getHeight();
		dragMap();
		zoom();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(map, x, y, size[sizeNum], size[sizeNum], null);
	}
}
