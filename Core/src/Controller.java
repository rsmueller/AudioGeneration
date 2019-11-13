import java.awt.event.*;
import java.util.ArrayList;

/**
 * In an effort to switch over to MVC structure, controller will now control the flow of program
 * Window should just be displaying of stuff and grabbing input.
 */
public class Controller implements Runnable, KeyboardListener, MouseWheelListener, MouseMotionListener {

    private SoundSynthesizer soundSynthesizer;
    private Thread mainThread;
    private ArrayList<InstrumentHandler> loadedInstruments;


    private Controller(){
        //Initializations first
        soundSynthesizer = new SoundSynthesizer();
        loadedInstruments = new ArrayList<>();
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
        loadedInstruments.add(new Piano(soundSynthesizer));

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
        for (InstrumentHandler instrument : loadedInstruments)
            instrument.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (InstrumentHandler instrument : loadedInstruments)
            instrument.keyReleased(e);
    }

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

    // ------------------End of call input methods on loaded instruments-----------------------


}
