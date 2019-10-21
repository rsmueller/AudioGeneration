import java.awt.event.KeyEvent;
import java.util.Dictionary;
import java.util.Hashtable;

public class Piano extends InstrumentHandler{
    public Dictionary<Integer, Integer> notes = new Hashtable<Integer, Integer>();

    {
        //top row of letters
        notes.put(81, 60);
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
