import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * In an effort to switch over to MVC structure, controller will now control the flow of program
 * Window should just be displaying of stuff and grabbing input.
 */
public class Controller implements Runnable{

    private SoundSynthesizer soundSynthesizer;
    private Thread mainThread;
    private LoadoutManager loadoutManager;

    private Window window;

    private Controller(){
        //Initializations first
        soundSynthesizer = new SoundSynthesizer();
        loadoutManager = new LoadoutManager(soundSynthesizer, this);
        mainThread = new Thread(this);
        //Window does a lot of stuff, best have last initialized.
        //Window adds Controller to KeyboardManager listeners,

        window = new Window(this);
        window.addKeyBoardListener(loadoutManager);
        window.addMouseMotionListener(loadoutManager);
        window.addMouseWheelListener(loadoutManager);

        mainThread.start();
    }

    /**
     * Begins the recording for the application's synthesizer.
     *
     * Called in reaction to UI event.
     */
    public void startRecording() {
        soundSynthesizer.getRecording().start();
        showIsRecording(true);
    }

    /**
     * Begins a new layer for the application recording.
     *
     * Called in reaction to UI event.
     */
    public void startLayer() {
        try {
            soundSynthesizer.getRecording().play();
            soundSynthesizer.getRecording().beginLayer();
        } catch (Exception e) {
            window.showError("Error", "Too many layers.");
        }
    }

    /**
     * Resets the application's recording.
     *
     * Called in reaction to UI event.
     */
    public void clearRecording() {
        soundSynthesizer.getRecording().clear();
        showIsRecording(false);
    }

    /**
     * Plays the application's recording.
     *
     * Called in reaction to UI event.
     */
    public void playRecording() {
        soundSynthesizer.getRecording().play();
    }

    /**
     * Saves the application's recording to a file.
     *
     * Called in reaction to UI event.
     */
    public void saveRecording() {
        File file = window.pickMIDIFile();
        if(file != null) {
            soundSynthesizer.getRecording().write(file);
        }
    }

    // Temporary
    public static void main(String[] args){
        new Controller();
    }

    @Override
    public void run() {
        //Set loadout to default
        loadoutManager.setToDefault();
    }

    void stop() {
        try{
            mainThread.join();
        }catch(Exception ignored){}
        System.exit(0);
    }

    public void onUserLoadoutChange(File selectedLoadout){
        loadoutManager.setCurrentLoadout(new Loadout(selectedLoadout));
    }

    public void createNewLoadout() throws FileNotFoundException {
        AddLoadoutWindow add = new AddLoadoutWindow(false, "");
        add.pack();
        add.setVisible(true);
        while (add.isVisible()){}
        window.updateLoadouts();
    }

    public void displayKeyPress(Note note, int keyCode){
        window.displayKeyPress(note, keyCode);
    }

    public void editDeleteLoadout() throws FileNotFoundException {
        EditDeleteWindow ED = new EditDeleteWindow();
        ED.pack();
        ED.setVisible(true);
        while (ED.isVisible()) {
        }
        window.updateLoadouts();
        for (File f : new File("temp").listFiles()) {
            f.delete();
        }
        new File("temp").delete();
    }

    /**
     * Display if recording or not to view
     */
    public void showIsRecording(boolean isRecording){
        window.showIsRecording(isRecording);
    }
}
