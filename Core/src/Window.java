import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the View of MVC, is the display and connection between input and output.
 */
public class Window extends JPanel{

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
    JMenuItem[] loadouts;


    private void createMenu(){
        menuBar = new JMenuBar();
        loadoutMenu = new JMenu("Loadout");
        loadoutMenu.getAccessibleContext().setAccessibleDescription(
                "The loadout of what plays on key presses."
        );
        menuBar.add(loadoutMenu);

        List<String> results = new ArrayList<String>();

        File resources = new File("resources");
        File[] loadoutFiles = resources.listFiles();
        loadouts = new JMenuItem[loadoutFiles.length];
        for (int i = 0; i < loadoutFiles.length; i++) {
            String fileName = loadoutFiles[i].getName();
            File file = loadoutFiles[i];
            loadouts[i] = new JMenuItem(new AbstractAction(fileName) {
                public void actionPerformed(ActionEvent e) {
                    controller.onUserLoadoutChange(file);
                }
            });
            loadoutMenu.add(loadouts[i]);
        }
        frame.setJMenuBar(menuBar);
    }


}
