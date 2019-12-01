import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

/**
 * Instruments are able to read a lot of different kinds of input
 * However it is not a big deal if they do not want to do anything on certain inputs
 * Such as mouse motion.
 */
public abstract class InstrumentHandler implements MouseWheelListener, MouseMotionListener {

    public int bend = 8192;

    protected SoundSynthesizer synth;

    protected String name;

    public int getCode(){
        return LoadoutManager.getInstrumentCode(this.getClass());
    }

    public void setSynth(SoundSynthesizer ss){
        synth = ss;
    }

    public abstract int getMidiInstrumentType();
}
