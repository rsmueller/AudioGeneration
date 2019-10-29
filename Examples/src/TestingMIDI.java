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

        Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        synth.loadInstrument(instruments[0]);
        MidiChannel[] midiChannels = synth.getChannels();
        midiChannels[0].programChange(20); // sets the instrument
        midiChannels[1].programChange(20); // sets the instrument

        int bend = 0;

        midiChannels[0].setPitchBend(8192); // default pitch bend, just shown here for reference
        midiChannels[0].noteOn(50, 100);
        midiChannels[1].noteOn(50, 100);

        Thread.sleep(500);

        for (int b = 0; b < 2000; b+=5){
            midiChannels[0].setPitchBend(8192 + b); //changing pitch while playing (works)
            System.out.printf("Bending %d\n", b);
            Thread.sleep(10);
        }

        Thread.sleep(100);
        midiChannels[0].programChange(21); // changing instrument while playing (doesn't work)
        Thread.sleep(1000);
        midiChannels[0].allSoundOff();

        /*for (int i = 20; i < 109; i++){
            midiChannels[4].noteOn(i, 60);
            Thread.sleep(100);
        }
        Thread.sleep(1000);
        midiChannels[4].allSoundOff();

         */
    }
}
