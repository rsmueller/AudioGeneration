import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Dictionary;
import java.util.Hashtable;

public class Piano extends InstrumentHandler{
    public Dictionary<Integer, Integer> notes = new Hashtable<>();

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

    public int key = 0; //0 is key of C, adding and subtracting by one move the key center chromatically

    public Piano(SoundSynthesizer synth) {
        super(synth);
        name = "Piano";
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (notes.get(keyCode) == -1) {
            synth.resetBend(this);
        } else {
            Note note = new Note(notes.get(keyCode) + key, 100, true);
            synth.playNote(this, note);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        Note note = new Note(notes.get(keyCode) + key, 100, false);
        synth.playNote(this, note);
    }

    @Override
    public int getMidiInstrumentType() {
        return 1;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        synth.bend(this, e.getWheelRotation()*-15); //this is where you can change the rate at which the bend is changed
    }
}
