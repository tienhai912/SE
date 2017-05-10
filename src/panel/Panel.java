package panel;

import java.awt.Graphics;

public abstract class Panel {

    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public Panel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public abstract void tick();

    public abstract void render(Graphics g);
}
