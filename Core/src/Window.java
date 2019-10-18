import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window extends JPanel implements Runnable {

    private JFrame frame;
    private KeyboardManager keyboardManager;
    private SoundSynthesizer soundSynthesizer;
    private Thread mainThread;


    public Window(){

        mainThread = new Thread(this);
        keyboardManager = new KeyboardManager();
        soundSynthesizer = new SoundSynthesizer();

        frame = new JFrame("Audio Generation");
        frame.addKeyListener(keyboardManager);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                stop();
            }
        });
        frame.add(this);
        frame.setSize(300,300);
        frame.setVisible(true);
        frame.toFront();

        mainThread.start();
        System.out.println("Window done");
    }

    // Temporary
    public static void main(String[] args){
        new Window();
        System.out.println("Main done");
    }

    @Override
    public void run() {
        // Create new instrument
        Piano piano = new Piano(soundSynthesizer);
        keyboardManager.addListener(piano);
    }

    private void stop(){
        try{
            mainThread.join();
        }catch(Exception ignored){}
        System.out.println("Stop done");
        System.exit(0);
    }
}
