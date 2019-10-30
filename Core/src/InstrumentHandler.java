import java.awt.event.MouseWheelListener;

public abstract class InstrumentHandler implements KeyboardListener, MouseWheelListener {

    public int bend = 8192;

    protected SoundSynthesizer synth;

    protected String name;

    public InstrumentHandler(SoundSynthesizer synth){
        this.synth = synth;

        synth.addInstrument(this);
    }

    public abstract int getMidiInstrumentType();
}
