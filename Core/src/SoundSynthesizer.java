import javax.sound.midi.*;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SoundSynthesizer {

    private Synthesizer synth;
    private MidiChannel[] channels;
    private Map<InstrumentHandler, MidiChannel> handlerChannelMap;

    private Recording recording;

    public SoundSynthesizer(){

        try {
            synth = MidiSystem.getSynthesizer();
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

        handlerChannelMap = new HashMap<InstrumentHandler, MidiChannel>();

        recording = new Recording();
    }

    public void addInstrument(InstrumentHandler instrument){
        int midiInstrumentType = instrument.getMidiInstrumentType();
        MidiChannel chosenChannel = null;
        for (MidiChannel channel : channels){
            if ( ! handlerChannelMap.containsValue(channel)){
                chosenChannel = channel;
                break;
            }
        }
        if (chosenChannel == null){
            System.out.println("Too many instruments loaded to add another instrument.");
            return;
        }
        chosenChannel.programChange(midiInstrumentType);
        handlerChannelMap.put(instrument, chosenChannel);
    }

    public void removeInstrument(InstrumentHandler instrument){
        handlerChannelMap.remove(instrument);
    }

    public void playNote(InstrumentHandler instrument, Note note){
        if (handlerChannelMap.containsKey(instrument)) {
            MidiChannel channel = handlerChannelMap.get(instrument);
            if (note.isOn()) {
                channel.noteOn(note.getNumber(), note.getVelocity());
            } else {
                channel.noteOff(note.getNumber(), note.getVelocity());
            }
            recording.addNote(note);
        }else{
            System.out.println(instrument.name + " has no designated channel.");
        }
    }

    public Recording getRecording() {
        return recording;
    }

}
