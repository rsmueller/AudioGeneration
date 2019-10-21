import javax.sound.midi.*;
import java.awt.event.KeyEvent;

public class Piano extends InstrumentHandler{

    public Piano(SoundSynthesizer synth) {
        super(synth);
        name = "Piano";
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        Note note = new Note(keyCode, 100, true);
        synth.playNote(this, note);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        Note note = new Note(keyCode, 100, false);
        synth.playNote(this, note);
    }

    @Override
    public int getMidiInstrumentType() {
        return 1;
    }
}
