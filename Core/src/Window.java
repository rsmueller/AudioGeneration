import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    JMenu recordingMenu;
    JMenuItem menuItem;
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

        menuBar.add(makeRecordingMenu());

        frame.setJMenuBar(menuBar);
    }

    /**
     * Creates the recording menu, with options to begin and play back the recording.
     *
     * @return The recording menu, to be added to the menu bar.
     */
    private JMenu makeRecordingMenu() {
        recordingMenu = new JMenu("Recording");

        // Option to begin recording
        menuItem = new JMenuItem("Start");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startRecording();
            }
        });
        recordingMenu.add(menuItem);

        // Option to begin recording another layer while playing previous layers
        menuItem = new JMenuItem("Start Layer");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startLayer();
            }
        });
        recordingMenu.add(menuItem);

        // Option to reset the recording
        menuItem = new JMenuItem("Clear");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.clearRecording();
            }
        });
        recordingMenu.add(menuItem);

        // Option to play the recording
        menuItem = new JMenuItem("Play");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.playRecording();
            }
        });
        recordingMenu.add(menuItem);

        // Option to save the recording to a file
        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.saveRecording();
            }
        });
        recordingMenu.add(menuItem);

        return recordingMenu;
    }

    /**
     * Prompts the user to pick a MIDI file to write to.
     *
     * @return The file selected by the user.
     */
    public File pickMIDIFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "MIDI Audio Files", "midi");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(frame);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            // add .midi extension if not entered
            File selection = chooser.getSelectedFile();
            String name = selection.getName();
            String extension = name.substring(name.lastIndexOf('.') + 1);
            if(!extension.equals("midi")) {
                return new File(selection.getAbsolutePath() + ".midi");
            } else {
                return selection;
            }
        } else {
            return null;
        }
    }

    /**
     * Displays an error to the user using a message dialog.
     *
     * @param title The title of the message dialog to show the user.
     * @param error The text of the message dialog to show the user.
     */
    public void showError(String title, String error) {
        JOptionPane.showMessageDialog(frame, error, title, JOptionPane.ERROR_MESSAGE);
    }

}
