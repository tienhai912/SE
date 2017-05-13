package se;

import UI.KeyManager;
import UI.MouseManager;
import display.Display;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JTextField;
import panel.Map;
import panel.Search;

public class App implements Runnable {

    private Display display;
    private int width, height;
    public String title;

    private boolean running = false;
    private Thread thread;

    private BufferStrategy bs;
    private Graphics g;

    // INPUT
    private KeyManager keyManager;
    private MouseManager mouseManager;

    private Map map;
    private Search search;

    JTextField text;

    public App(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    private void init() {
        keyManager = new KeyManager();
        mouseManager = new MouseManager();

        display = new Display(title, width, height);
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getFrame().addMouseWheelListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseWheelListener(mouseManager);
        

        map = new Map(0, 0, 2000, 2000, display, mouseManager);
        search = new Search(0, 0, 0, 0, display, mouseManager, keyManager);
    }

    @Override
    public void run() {
        init();
        int fps = 60;
        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
                ticks++;
            }

            if (timer >= 1000000000) {
                //System.out.println("FPS = " + ticks);
                timer = 0;
                ticks = 0;
            }
        }
        stop();
    }

    private void tick() {
        map.tick();
        search.tick();
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        // Clear Screen
        g.clearRect(0, 0, (int) display.getFrame().getBounds().getWidth(), (int) display.getFrame().getBounds().getHeight());
        // Draw Here!
        map.render(g);
        search.render(g);
        // End Drawing!
        bs.show();
        g.dispose();
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public KeyManager getKeymanager() {
        return keyManager;
    }

    public MouseManager getMousemanager() {
        return mouseManager;
    }
}
