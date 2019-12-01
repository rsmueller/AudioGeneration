import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardManager implements KeyListener {

    private ArrayList<KeyboardListener> listeners;
    private boolean[] pressedKeyCodes;

    public KeyboardManager(){
        listeners = new ArrayList<KeyboardListener>();
        pressedKeyCodes = new boolean[93];
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

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (pressedKeyCodes.length <= keyCode || pressedKeyCodes[keyCode])
            return;
        else
            pressedKeyCodes[keyCode] = true;

        for (KeyboardListener listener : listeners)
            listener.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (pressedKeyCodes.length <= keyCode)
            return;
        else
            pressedKeyCodes[keyCode] = false;


        for (KeyboardListener listener : listeners)
            listener.keyReleased(e);
    }
}
