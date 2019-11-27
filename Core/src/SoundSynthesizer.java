import javax.sound.midi.*;
import java.util.HashMap;
import java.util.Map;

public class SoundSynthesizer {

    private Synthesizer synth;
    private MidiChannel[] channels;
    private Map<Integer, MidiChannel> instrumentChannelMap;

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
    }

    public void removeInstrument(int instrument){
        instrumentChannelMap.remove(instrument);
    }

    public void clearInstruments(){instrumentChannelMap.clear();}

    void playNote(Note note){
        int instrument = note.getInstrument();
        if (instrumentChannelMap.containsKey(instrument)) {
            MidiChannel channel = instrumentChannelMap.get(instrument);
            if (note.isOn()) {
                channel.noteOn(note.getNumber(), note.getVelocity());
            } else {
                channel.noteOff(note.getNumber(), note.getVelocity());
            }
        }else{
            String instrumentName = LoadoutManager.getInstrumentClass(instrument).getName();
            System.out.println(instrumentName + " has no designated channel.");
        }
    }

    /*public void bend(InstrumentHandler instrument, int amount){ // outdated scroll wheel bend method
        if (handlerChannelMap.containsKey(instrument)){
            MidiChannel channel = handlerChannelMap.get(instrument);
            if (instrument.bend + amount > 0 && instrument.bend + amount < 16383) {
                instrument.bend += amount;
            }
            channel.setPitchBend(instrument.bend);
            System.out.println(instrument.bend);
        }
    }*/

    /*
    void setBend(int instrument, int amount){
        if (instrumentChannelMap.containsKey(instrument)) {
            MidiChannel channel = instrumentChannelMap.get(instrument);
            instrument.bend = amount;
            channel.setPitchBend(instrument.bend);
            //System.out.println(instrument.bend);
        }
    }

    void resetBend(InstrumentHandler instrument) {
        if (instrumentChannelMap.containsKey(instrument)) {
            MidiChannel channel = instrumentChannelMap.get(instrument);
            instrument.bend = 8192;
            channel.setPitchBend(instrument.bend);
        }
    }*/

}
