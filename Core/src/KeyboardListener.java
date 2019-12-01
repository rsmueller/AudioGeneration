import java.awt.event.KeyEvent;

/**
 * Java does not have a built in way to check effectively that a key
 * was pressed or released (specifically released).
 */
public interface KeyboardListener {
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);
}
