import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

/**
 * Instruments are able to read a lot of different kinds of input
 * However it is not a big deal if they do not want to do anything on certain inputs
 * Such as mouse motion.
 */
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
