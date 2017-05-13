package UI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    char c;

    @Override
    public void keyPressed(KeyEvent e) {
        c = e.getKeyChar();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public char getC() {
        return c;
    }
}
