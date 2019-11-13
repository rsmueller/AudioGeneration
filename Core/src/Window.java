import javax.sound.midi.ControllerEventListener;
import javax.swing.*;
import java.awt.event.*;

/**
 * Handles the View of MVC, is the display and connection between input and output.
 */
public class Window extends JPanel implements KeyboardListener, MouseWheelListener, MouseMotionListener {

    private Controller controller;


    Window(Controller c){

        KeyboardManager keyboardManager = new KeyboardManager();
        keyboardManager.addListener(c);

        controller = c;

        JFrame frame = new JFrame("Audio Generation");
        frame.addKeyListener(keyboardManager);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                controller.stop();
            }
        });
        frame.add(this);
        frame.setSize(500,1000);
        frame.setVisible(true);
        frame.toFront();

        frame.addMouseWheelListener(this);
        frame.addMouseMotionListener(this);

    }


    @Override
    public void keyPressed(KeyEvent e) {
        controller.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        controller.keyReleased(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        controller.mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controller.mouseWheelMoved(e);
    }

}
