import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class Piano extends InstrumentHandler{
    private HashMap<Integer, Integer> notes = new HashMap<>();

    {   // keys tuned to the key of C with 'Q' being middle C
        notes.put(32, -1); // space bar ; note (-1) is reset bend, not an actual note
        //middle row of letters
        notes.put(65, 48);
        notes.put(83, 49);
        notes.put(68, 50);
        notes.put(70, 51);
        notes.put(71, 52);
        notes.put(72, 53);
        notes.put(74, 54);
        notes.put(75, 55);
        notes.put(76, 56);
        notes.put(59, 57);
        notes.put(222, 58);
        notes.put(10, 59);
        //top row of letters
        notes.put(81, 60); //middle C
        notes.put(87, 61);
        notes.put(69, 62);
        notes.put(82, 63);
        notes.put(84, 64);
        notes.put(89, 65);
        notes.put(85, 66);
        notes.put(73, 67);
        notes.put(79, 68);
        notes.put(80, 69);
        notes.put(91, 70);
        notes.put(93, 71);
        //number row
        notes.put(49, 72);
        notes.put(50, 73);
        notes.put(51, 74);
        notes.put(52, 75);
        notes.put(53, 76);
        notes.put(54, 77);
        notes.put(55, 78);
        notes.put(56, 79);
        notes.put(57, 80);
        notes.put(48, 81);
        notes.put(45, 82);
        notes.put(61, 83);
        //backspace to finish the scale
        notes.put(8, 84);
    }

    private int maxY = 492; // window height - 8
    private int minY = 30;

    private int maxX = 492; // window width - 8
    private int minX = 7;

    private int velocity = 100;

    private int key = 0; //0 is key of C, adding and subtracting by one move the key center chromatically

    public Piano(SoundSynthesizer synth) {
        super(synth);
        name = "Piano";
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if(notes.containsKey(keyCode)) {
            if (notes.get(keyCode) > 0) {
                Note note = new Note(notes.get(keyCode) + key, velocity, true);
                synth.playNote(this, note);
            } else {
                if (notes.get(keyCode) == -1) {
                    synth.resetBend(this);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if(notes.containsKey(keyCode)) {
            if (notes.get(keyCode) > 0) {
                Note note = new Note(notes.get(keyCode) + key, velocity, false);
                synth.playNote(this, note);
            }
        }
    }

    @Override
    public int getMidiInstrumentType() {
        return 1;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /*int sign = e.getWheelRotation()/Math.abs(e.getWheelRotation());
        synth.bend(this, (int)(Math.pow(e.getWheelRotation(), 2)*-50) * sign); //this is where you can change the rate at which the bend is changed*/
    } // not a good way to do it, so its replaced with the mouse position method

    @Override
    public void mouseDragged(MouseEvent e) { }//not necessary atm

    @Override
    public void mouseMoved(MouseEvent e) {
        synth.setBend(this, (int) ((maxY-minY-(e.getY() - minY)) / (double) (maxY - minY) * 16383));
        velocity = ((int) ((e.getX()-minX) /(double) (maxX - minX) * 117)) + 10;
        //System.out.println(((int) ((e.getX()-minX) /(double) (maxX - minX) * 90)) + 10);
    }

    @Override
    public void componentResized(ComponentEvent e) { //changes scale of bend & velocity when window size is changed
        maxY = e.getComponent().getHeight()-8;
        maxX = e.getComponent().getWidth()-8;
        //System.out.println(e.paramString());
        //System.out.println(maxX + " " + maxY);
    }

    @Override
    public void componentMoved(ComponentEvent e) {}//not necessary atm

    @Override
    public void componentShown(ComponentEvent e) {}//not necessary atm

    @Override
    public void componentHidden(ComponentEvent e) {}// not necessary atm
}
