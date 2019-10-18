import javax.sound.midi.*;

public class TestingMIDI {
    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {

        //https://docs.oracle.com/javase/tutorial/sound/overview-MIDI.html

        //Midi messages small things with two bytes of data and lots of integer parameters.

        //Midi events contain wrappers for the messages. As well as timing information.
        //public MidiEvent(MidiMessage message, long tick)

        //Midi file stores midi events arranged into tracks.
        //File represents one musical composition.
        //Each track represents a part such as might have been played by a single instrumentalist.
        //Each note that the instrumentalist plays is represented by at least two events. (Note on and Note off)

        //Classes that implement the MidiDevice interface can transmit and receive messages.

        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();

        //Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        //synth.loadInstrument(instruments[0]);
        MidiChannel[] midiChannels = synth.getChannels();
        midiChannels[4].programChange(25); // sets the instrument
        midiChannels[1].programChange(1); // sets the instrument

        //begins playing here.

        // a chord
        midiChannels[4].noteOn(46, 100);
        Thread.sleep(50);
        midiChannels[4].noteOn(50, 100);
        Thread.sleep(50);
        midiChannels[4].noteOn(53, 100);
        Thread.sleep(50);
        midiChannels[4].noteOn(57, 100);
        Thread.sleep(1500);
        midiChannels[4].allSoundOff();

        for (int i = 20; i < 109; i++){
            midiChannels[4].noteOn(i, 60);
            if (i == 50)
                midiChannels[4].programChange(1);
            Thread.sleep(100);
        }
        Thread.sleep(1000);
        midiChannels[4].allSoundOff();
    }
}
