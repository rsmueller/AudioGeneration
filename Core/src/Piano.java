import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;

public class Piano extends InstrumentHandler{
    public Dictionary<Integer, Integer> notes = new Hashtable<Integer, Integer>();

    {   // keys tuned to the key of C with 'Q' being middle C
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
        //numbers
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

    public Piano(SoundSynthesizer synth) {
        super(synth);
        name = "Piano";
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        Note note = new Note(notes.get(keyCode), 100, true);
        synth.playNote(this, note);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        Note note = new Note(notes.get(keyCode), 100, false);
        synth.playNote(this, note);
    }

    @Override
    public int getMidiInstrumentType() {
        return 1;
    }
}
