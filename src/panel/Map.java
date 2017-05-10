package panel;

import UI.MouseManager;
import java.awt.image.BufferedImage;

import display.Display;
import display.ImageLoader;
import java.awt.Graphics;

import UI.KeyManager;

public class Map extends Panel {

	private int size[];
	private int preX, preY, frameWidth, frameHeight;
	MouseManager mouseManager;
	KeyManager keyManager;
	private int mapNum;
	private String mapPath[];
	private Display display;
	private BufferedImage map;

	public Map(int x, int y, int width, int height, Display display, MouseManager mouseManager, KeyManager keyManager) {
		super(x, y, width, height);
		preX = 0;
		preY = 0;
		this.mouseManager = mouseManager;
		this.keyManager = keyManager;
		this.display = display;
		mapNum = 0;
		size = new int[2];
		size[0] = 2000;
		size[1] = 4000;
		mapPath = new String[2];
		mapPath[0] = "/2000x2000.png";
		mapPath[1] = "/4000x4000.png";
		map = ImageLoader.loadImage(mapPath[mapNum]);
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

	public void changeMapSize() {
		if (mouseManager.getNotches() < 0) {
			if (mapNum < mapPath.length - 1) {
				mapNum++;
				map = ImageLoader.loadImage(mapPath[mapNum]);
			}
		} else {
			if (mapNum > 0) {
				mapNum--;
				map = ImageLoader.loadImage(mapPath[mapNum]);
			}
		}

	}

	public void rescale() {
		if (x > 0)
			x = 0;
		else if (x < frameWidth - size[mapNum])
			x = frameWidth - size[mapNum];
		if (y > 0)
			y = 0;
		else if (y < frameHeight - size[mapNum])
			y = frameHeight - size[mapNum];
	}

	@Override
	public void tick() {
		frameWidth = (int) display.getFrame().getBounds().getWidth();
		frameHeight = (int) display.getFrame().getBounds().getHeight();
		updateX();
		updateY();
		changeMapSize();
		rescale();
		System.out.println(x + " " + frameWidth + " " + size[mapNum]);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(map, x, y, size[mapNum], size[mapNum], null);
	}
}
