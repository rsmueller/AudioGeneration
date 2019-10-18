import javax.sound.midi.*;
public class Piano extends InstrumentHandler{

    public Piano(SoundSynthesizer synth) {
        super(synth);
        name = "Piano";
    }

    @Override
    public void keyPressed(int keyCode) {
        Note note = new Note(46, 100, true);
        synth.playNote(this, note);
    }

    @Override
    public void keyReleased(int keyCode) {
        Note note = new Note(46, 100, false);
        synth.playNote(this, note);
    }

    @Override
    public int getMidiInstrumentType() {
        return 1;
    }
}
