import javax.sound.midi.*;

public class TestingMIDI {
    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {

        //https://docs.oracle.com/javase/tutorial/sound/overview-MIDI.html

        //Midi messages small things with two bytes of data and lots of integer parameters.

        //Midi events contain wrappers for the messages. As well as timing information.
        //public MidiEvent(MidiMessage message, long tick)

        //Midi file stores midievents arranged into tracks.
        //File represents one musical composition.
        //Each track represents a part such as might ahve been playe by a single instrumentalist.
        //Each note that the instrumentalist plays is represented by at least two events. (Note on and Note off)

        //Classes that implement the MidiDevice interface can transmit and recieve messages.

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();

        Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        synth.loadInstrument(instruments[0]);
        MidiChannel[] midiChannels = synth.getChannels();
        midiChannels[4].programChange(10); // sets the instrument
        //megins playing here.
        midiChannels[4].noteOn(46, 100);
        Thread.sleep(50);
        midiChannels[4].noteOn(50, 100);
        Thread.sleep(50);
        midiChannels[4].noteOn(53, 100);
        Thread.sleep(50);
        midiChannels[4].noteOn(57, 100);
        Thread.sleep(5000);
        midiChannels[4].noteOff(46, 60);
        midiChannels[4].noteOff(50, 60);
        midiChannels[4].noteOff(53, 60);
        midiChannels[4].noteOff(57, 60);

        for (int i = 20; i < 80; i++){
            midiChannels[4].noteOn(i, 100);
            Thread.sleep(50);
        }

        midiChannels[4].allSoundOff();

    }
}
