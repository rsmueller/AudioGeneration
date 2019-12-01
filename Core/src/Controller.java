import java.awt.event.*;
import java.util.ArrayList;

/**
 * In an effort to switch over to MVC structure, controller will now control the flow of program
 * Window should just be displaying of stuff and grabbing input.
 */
public class Controller implements Runnable{

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
        window.addKeyBoardListener(loadoutManager);
        window.addMouseMotionListener(loadoutManager);
        window.addMouseWheelListener(loadoutManager);

        mainThread.start();
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

    void stop(){
        try{
            mainThread.join();
        }catch(Exception ignored){}
        System.exit(0);
    }

}
