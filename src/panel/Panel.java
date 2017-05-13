package panel;

import display.Display;
import java.awt.Graphics;
import javax.swing.JFrame;

public abstract class Panel extends JFrame{

	private static final long serialVersionUID = 1L;
	protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Display display;

    public Panel(int x, int y, int width, int height, Display display) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.display = display;
    }

    public abstract void tick();

    public abstract void render(Graphics g);
}
