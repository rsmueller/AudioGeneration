import java.awt.event.*;
import java.util.ArrayList;

/**
 * In an effort to switch over to MVC structure, controller will now control the flow of program
 * Window should just be displaying of stuff and grabbing input.
 */
public class Controller implements Runnable, KeyboardListener{

    private SoundSynthesizer soundSynthesizer;
    private Thread mainThread;
    private LoadoutManager loadoutManager;

    private Controller(){
        //Initializations first
        soundSynthesizer = new SoundSynthesizer();
        loadoutManager = new LoadoutManager(soundSynthesizer);
        mainThread = new Thread(this);
        //Window does a lot of stuff, best have last initialized.
        //Window adds Controller to KeyboardManager listeners,
        Window window = new Window(this);

        mainThread.start();
    }

    // Temporary
    public static void main(String[] args){
        new Controller();
    }

    @Override
    public void run() {
        // Create new instrument
        //loadedInstruments.add(new Piano(soundSynthesizer));
        Loadout piano = new Loadout("C:\\Users\\thatg\\Documents\\GitHub\\AudioGeneration\\resources\\piano.layout");
        loadoutManager.setCurrentLoadout(piano);
    }

    void stop(){
        try{
            mainThread.join();
        }catch(Exception ignored){}
        System.exit(0);
    }

    // ------------------Call input methods on loaded instruments-----------------------

    @Override
    public void keyPressed(KeyEvent e) {
        int instrument = loadoutManager.getInstrument(e.getKeyCode());
        int noteNum = loadoutManager.getNote(e.getKeyCode());
        Note note = new Note(noteNum, 100, instrument, true);
        soundSynthesizer.playNote(note);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int instrument = loadoutManager.getInstrument(e.getKeyCode());
        int noteNum = loadoutManager.getNote(e.getKeyCode());
        Note note = new Note(noteNum, 100, instrument, false);
        soundSynthesizer.playNote(note);
    }

    //Dont currently have a solution for this since we want to possibly bind keys to other instruments
    //Possibly a mouse_bound instrument per loadout defind at top of file.
    /*
    @Override
    public void mouseDragged(MouseEvent e) {
        for (InstrumentHandler instrument : loadedInstruments)
            instrument.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (InstrumentHandler instrument : loadedInstruments)
            instrument.mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        for (InstrumentHandler instrument : loadedInstruments)
            instrument.mouseWheelMoved(e);
    }
    */
    // ------------------End of call input methods on loaded instruments-----------------------


}
