import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the View of MVC, is the display and connection between input and output.
 */
public class Window extends JPanel {

    private Controller controller;
    private JFrame frame;
    private KeyboardManager keyboardManager;
    private JLabel lblPressedKeys;
    private JLabel lblIsRecording;
    private final int LBL_PRESSED_LEYS_MAX_LINES = 10;
    public static int width = 300;
    public static int height = 500;

    Window(Controller c){

        keyboardManager = new KeyboardManager();

        controller = c;

        lblPressedKeys = new JLabel();
        lblIsRecording = new JLabel();

        frame = new JFrame("Instrument Player");
        createMenuBar();
        frame.addKeyListener(keyboardManager);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                controller.stop();
            }
        });
        frame.add(this);
        frame.setSize(width,height);
        frame.setLayout(new BorderLayout());

        frame.add(lblPressedKeys, BorderLayout.SOUTH);
        frame.add(lblIsRecording, BorderLayout.NORTH);

        frame.setVisible(true);
        frame.toFront();

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                width = frame.getWidth();
                height = frame.getHeight();
            }
        });

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


    /**
     * Creates the menu bar for the application
     */
    private void createMenuBar(){
        menuBar = new JMenuBar();
        loadoutMenu = new JMenu("Loadout");
        loadoutMenu.getAccessibleContext().setAccessibleDescription(
                "The loadout of what plays on key presses."
        );
        menuBar.add(loadoutMenu);

        List<String> results = new ArrayList<String>();

        updateLoadouts();

        menuItem = new JMenuItem("Create New...");
        menuItem.addActionListener(e -> {
            try {
                controller.createNewLoadout();
            } catch (FileNotFoundException ex) {}
        });
        loadoutMenu.add(menuItem);

        menuItem = new JMenuItem("Edit/Delete Loadouts");
        menuItem.addActionListener(e -> {
            try {
                controller.editDeleteLoadout();
            } catch (FileNotFoundException ex) {}
        });
        loadoutMenu.add(menuItem);

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

    /**
     * Creates the loadout menu which the user can use to select a loadout to play.
     * Checks resources file and creates a menu item for each .loadout file.
     */
    public void updateLoadouts(){
        int items = loadoutMenu.getItemCount()-2;
        for (int i = 0; i < items; i++){
            loadoutMenu.remove(0);
        }
        File resources = new File("resources");
        File[] loadoutFiles = resources.listFiles();
        try {
            loadouts = new JMenuItem[loadoutFiles.length];
            for (int i = 0; i < loadoutFiles.length; i++) {
                String fileName = loadoutFiles[i].getName().split("\\.")[0];
                File file = loadoutFiles[i];
                loadouts[i] = new JMenuItem(new AbstractAction(fileName) {
                    public void actionPerformed(ActionEvent e) {
                        controller.onUserLoadoutChange(file);
                    }
                });
                loadoutMenu.add(loadouts[i], i);
            }
        }
        catch (NullPointerException e){
            System.out.println("Loadouts folder empty, please re-download or create loadout files.");
        }
    }


    //Solved multiline problem with
    //this stack overflow solution
    //https://stackoverflow.com/questions/2152742/java-swing-multiline-labels
    public void displayKeyPress(Note note, int keyCode){
        String prevText = lblPressedKeys.getText();
        if (prevText.equals(""))
            prevText = "<br>";
        String[] lines = prevText.split("<br>");
        System.out.println("Current lines: "+lines.length);
        if (lines.length > LBL_PRESSED_LEYS_MAX_LINES){
            StringBuilder temp = new StringBuilder();
            int start_position = lines.length - LBL_PRESSED_LEYS_MAX_LINES;
            for (int i = start_position; i < lines.length; i++) {
                temp.append(lines[i] + "<br>");
            }
            prevText = temp.toString();
        }
        String instrumentName;
        instrumentName = LoadoutManager.getInstrumentClass(note.getInstrument()).getName();
        String text = String.format(
                "<html>%sKeycode: %s Note: %s Instrument: %s<br>",
                prevText, keyCode, note.getNumber(), instrumentName);
        lblPressedKeys.setText(text);
    }

    /**
     * Display if recording to user
     * @param isRecording
     */
    public void showIsRecording(boolean isRecording){
        if (isRecording){
            lblIsRecording.setText("Recording...");
        }else{
            lblIsRecording.setText("");
        }
    }
}
