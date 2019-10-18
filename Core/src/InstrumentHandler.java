public abstract class InstrumentHandler implements KeyboardListener{

    protected SoundSynthesizer synth;

    protected String name;

    public InstrumentHandler(SoundSynthesizer synth){
        this.synth = synth;

        synth.addInstrument(this);
    }

    public abstract int getMidiInstrumentType();
}
