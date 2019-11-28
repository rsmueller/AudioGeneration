import javax.sound.midi.ControllerEventListener;
import javax.swing.*;
import java.awt.event.*;

/**
 * Handles the View of MVC, is the display and connection between input and output.
 */
public class Window extends JPanel {

    private Controller controller;
    private JFrame frame;
    private KeyboardManager keyboardManager;

    Window(Controller c){

        keyboardManager = new KeyboardManager();

        controller = c;

        frame = new JFrame("Audio Generation");
        createMenu();
        frame.addKeyListener(keyboardManager);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                controller.stop();
            }
        });
        frame.add(this);
        frame.setSize(800,500);
        frame.setVisible(true);
        frame.toFront();

    }

    public void addMouseWheelListener(MouseWheelListener e){
        frame.addMouseWheelListener(e);
    }

    public void addMouseMotionListener(MouseMotionListener e){
        frame.addMouseMotionListener(e);
    }

    public void addKeyBoardListener(KeyboardListener e){
        keyboardManager.addListener(e);
    }
    //Would like a menu where you can choose to either use a premade loadout or create your own.
    // Menu: Choose loadout
    //      ->Piano
    //      ->Guitar
    //      ->Custom
    //          ->my_custom_loadout
    //          ->New Custom Loadout
    //---------------------------------
    //Menu instance variables
    JMenuBar menuBar;
    JMenu loadoutMenu;
    JMenuItem menuItem;

    private void createMenu(){
        menuBar = new JMenuBar();
        loadoutMenu = new JMenu("Loadout");
        loadoutMenu.getAccessibleContext().setAccessibleDescription(
                "The loadout of what plays on key presses."
        );
        menuBar.add(loadoutMenu);

        menuItem = new JMenuItem("Piano");
        loadoutMenu.add(menuItem);
        frame.setJMenuBar(menuBar);
    }

}
