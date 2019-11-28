import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;

public class SoundSynthesizer {

    private Synthesizer synth;
    private MidiChannel[] channels;
    private Map<Integer, MidiChannel> instrumentChannelMap;
    private InstrumentHandler mouseInstrument;

    SoundSynthesizer(){
        try {
            synth = MidiSystem.getSynthesizer();
            System.out.println("Synthesizer Latency: "+synth.getLatency());
            synth.open();
        }catch(MidiUnavailableException midiError){
            System.out.println("Default Synthesizer is unavailable");
            //midiChannel[0].programChange(0);
            return;
        }
        channels = synth.getChannels();
        // Cannot use channel 10
        MidiChannel[] temp = new MidiChannel[channels.length-1];
        for (int i = 0; i < 9; i++)
            temp[i] = channels[i];
        for (int i = 10; i < channels.length; i++)
            temp[i-1] = channels[i];
        channels = temp;

        instrumentChannelMap = new HashMap<Integer, MidiChannel>();
    }

    public void setMouseInstrument(InstrumentHandler instrumentHandler){
        this.mouseInstrument = instrumentHandler;
        addInstrument(instrumentHandler.getCode());
    }

    void addInstrument(int instrument){
        //int midiInstrumentType = instrument.getMidiInstrumentType();
        //New system takes over, going straight to the integer representation of the instrument.
        MidiChannel chosenChannel = null;
        for (MidiChannel channel : channels){
            if ( ! instrumentChannelMap.containsValue(channel)){
                chosenChannel = channel;
                break;
            }
        }
        if (chosenChannel == null){
            System.out.println("Too many instruments loaded to add another instrument.");
            return;
        }
        chosenChannel.programChange(instrument);
        instrumentChannelMap.put(instrument, chosenChannel);
        System.out.println("Put an instrument "+instrument);
    }

    public void removeInstrument(int instrument){
        instrumentChannelMap.remove(instrument);
    }

    public void clearInstruments(){
        instrumentChannelMap.clear();}

    void playNote(Note note){

        int instrument = note.getInstrument();
        if (instrumentChannelMap.containsKey(instrument)) {
            MidiChannel channel = instrumentChannelMap.get(instrument);
            if (note.isOn()) {
                channel.noteOn(note.getNumber(), note.getVelocity());
            } else {
                channel.noteOff(note.getNumber(), note.getVelocity());
            }
        }else {
            Class instrClass = LoadoutManager.getInstrumentClass(instrument);
            String instrumentName = instrClass.getName();
            System.out.println(instrumentName + " has no designated midi channel in the synthesizer.");
        }
    }

    public void bend(int amount){ // outdated scroll wheel bend method
        int mouseInstrumentCode = mouseInstrument.getCode();

        if (instrumentChannelMap.containsKey(mouseInstrumentCode)){
            MidiChannel channel = instrumentChannelMap.get(mouseInstrumentCode);
            if (mouseInstrument.bend + amount > 0 && mouseInstrument.bend + amount < 16383) {
                mouseInstrument.bend += amount;
            }
            channel.setPitchBend(mouseInstrument.bend);
            System.out.println(mouseInstrument.bend);
        }
    }


    void setBend(int amount){
        int mouseInstrumentCode = mouseInstrument.getCode();

        if (instrumentChannelMap.containsKey(mouseInstrumentCode)) {
            MidiChannel channel = instrumentChannelMap.get(mouseInstrumentCode);
            mouseInstrument.bend = amount;
            channel.setPitchBend(mouseInstrument.bend);
            //System.out.println(instrument.bend);
        }
    }

    void resetBend(int instrument) {
        int mouseInstrumentCode = mouseInstrument.getCode();

        if (instrumentChannelMap.containsKey(mouseInstrumentCode)) {
            MidiChannel channel = instrumentChannelMap.get(mouseInstrumentCode);
            mouseInstrument.bend = 8192;
            channel.setPitchBend(mouseInstrument.bend);
        }
    }

}
