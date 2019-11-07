import javax.sound.midi.*;
import java.util.*;
import java.time.*;

class Recording {

    // Timestamp of the first note in the recording
    private long startTime = 0;

    // Internal storage of notes and times
    private List<Note> notes;
    private List<Long> timestamps;

    private Sequencer sequencer;

    /**
     * Creates a new empty recording
     */
    Recording() {
        notes = new ArrayList<Note>();
        timestamps = new ArrayList<Long>();

        // Try to get the default synthesizer
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException | NullPointerException e) {
            // Print an error message if sequencer is unavailable
            // TODO better error handing
            System.out.println("MIDI sequencer is unavailable");
        }
    }

    /**
     * Adds the given note to the recording and creates a timestamp for it.
     *
     * @param note The note event to add to this recording.
     */
    public void addNote(Note note) {
        long currentTime = Instant.now().toEpochMilli();
        if(startTime == 0) {
            startTime = currentTime;
        }

        notes.add(note);
        timestamps.add(currentTime);
    }

    /**
     * Plays the recording through the default MIDI sequencer.
     */
    public void play() {
        try {
            sequencer.setSequence(createSequence());
            sequencer.start();
        } catch (InvalidMidiDataException e) {
            System.out.println("Could not play recording.");
        }
    }

    /**
     * Converts the recording to a MIDI sequence.
     *
     * @return a MIDI Sequence representing this recording.
     * @throws InvalidMidiDataException if there is an error creating the MIDI sequence.
     */
    private Sequence createSequence() throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.SMPTE_30, 10);
        Track track = sequence.createTrack();
        for(int i = 0; i < notes.size(); i++) {
            ShortMessage message = noteMessage(notes.get(i));
            long tick = milliToTick(timestamps.get(i));
            MidiEvent event = new MidiEvent(message, tick);
            track.add(event);
        }
        return sequence;
    }

    /**
     * Creates a MIDI message from the given note.
     *
     * @param note the note to convert into a MIDI message.
     * @return a midi mesage describing the given note.
     * @throws InvalidMidiDataException if there is an error creating the message.
     */
    private ShortMessage noteMessage(Note note) throws InvalidMidiDataException {
        int on = note.isOn() ? ShortMessage.NOTE_ON : ShortMessage.NOTE_OFF;
        return new ShortMessage(on, 0, note.getNumber(), note.getVelocity());
    }

    /**
     * Converts a time in milliseconds to a time in MIDI ticks.
     * Currently based on a 30 FPS, 10 resolution time setup.
     *
     * @param millis a timestamp in milliseconds.
     * @return the MIDI tick closest to the given time in milliseconds.
     */
    private long milliToTick(long millis) {
        long offset = millis - startTime;
        long ticksPerSecond = 30 * 10;
        long millisPerSecond = 1000;

        return (long) ((double) offset / millisPerSecond * ticksPerSecond);
    }

}