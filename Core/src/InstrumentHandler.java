import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

public abstract class InstrumentHandler implements KeyboardListener, MouseWheelListener, MouseMotionListener {

    public int bend = 8192;

    protected SoundSynthesizer synth;

    protected String name;

    public InstrumentHandler(SoundSynthesizer synth){
        this.synth = synth;

        synth.addInstrument(this);
    }

    public abstract int getMidiInstrumentType();
}
