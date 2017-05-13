package UI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseManager implements MouseListener, MouseMotionListener, MouseWheelListener {

    private int x, y;
    private boolean Pressed = false, zoomIn = false, zoomOut = false;
    private int notches;
    private long zoomTimer = 500, lastZoom;

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
        if (notches != 0) {
            zoomTimer += System.currentTimeMillis() - lastZoom;
            lastZoom = System.currentTimeMillis();
            if (zoomTimer < 100) {
                return;
            }
            zoomTimer = 0;
            if (notches < 0) {
                zoomIn = true;
            } else {
                zoomOut = true;
            }
        }
    }

    public void setZoom() {
        zoomIn = zoomOut = false;
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

    public boolean isZoomIn() {
        return zoomIn;
    }

    public boolean isZoomOut() {
        return zoomOut;
    }

}
