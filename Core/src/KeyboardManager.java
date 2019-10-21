import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardManager implements KeyListener {

    private ArrayList<KeyboardListener> listeners;

    private ArrayList<Integer> pressedKeyCodes;

    public KeyboardManager(){
        listeners = new ArrayList<KeyboardListener>();
        pressedKeyCodes = new ArrayList<Integer>();
    }

    public void addListener(KeyboardListener listener){
        listeners.add(listener);
    }

    public void removeListener(KeyboardListener listener){
        listeners.remove(listener);
    }

    // Do not see why to care about this.
    @Override
    public void keyTyped(KeyEvent e) {
        // System.out.println(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (pressedKeyCodes.contains(keyCode))
            return;
        for (KeyboardListener listener : listeners)
            listener.keyPressed(e);
        pressedKeyCodes.add(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (KeyboardListener listener : listeners)
            listener.keyReleased(e);
        pressedKeyCodes.remove((Integer)keyCode);
    }
}
