package UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {

	private int x, y;
	private boolean Pressed = false;
	private int notches;

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			x = e.getX();
			y = e.getY();
			Pressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Pressed = false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

		notches = e.getWheelRotation();
	}

	// GETTER AND SETTER

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isPressed() {
		return Pressed;
	}

	public int getNotches() {
		return notches;
	}
	
}
