import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.time.*;

class Recording {

    private static int MAX_14 = 16383; // 2 ^ 14 - 1
    private static int FILE_TYPE = 1; // Midi Type 1 file, supports multiple tracks

    // Timestamp of the first note in the recording
    private long startTime = 0;

    private int currentChannel = 0;

    // Internal storage of notes and times
    private List<ShortMessage> messages;
    private List<Long> timestamps;

    private Sequencer sequencer;

    /**
     * Creates a new empty recording
     */
    Recording() {
        messages = new ArrayList<ShortMessage>();
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

        try {
            ShortMessage message = noteMessage(note);
            messages.add(message);
            timestamps.add(currentTime - startTime);
        } catch (InvalidMidiDataException e) {
            // Should never be reached
            e.printStackTrace();
        }
    }

    /**
     * Adds the given bend amount to the recording and creates a timestamp for it.
     *
     * Will not begin recording if it has not been started.
     *
     * @param amount The amount to set pitch bend to.
     */
    public void setBend(int amount) {
        if(startTime > 0) {
            long currentTime = Instant.now().toEpochMilli();
            try {
                ShortMessage message = bendMessage(amount);
                messages.add(message);
                timestamps.add(currentTime - startTime);
            } catch (InvalidMidiDataException e) {
                // Should never be reached
                e.printStackTrace();
            }
        }
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
     * Resets the time and increments the channel so a new layer can be recorded.
     *
     * @throws Exception if there are too many layers in the recording
     */
    public void beginLayer() throws Exception {
        currentChannel++;
        if(currentChannel < 16) { // MIDI has a max of 16 channels
            startTime = Instant.now().toEpochMilli();
        } else {
            // This should be handled in UI code
            throw new Exception("Too many layers");
        }
    }

    /**
     * Writes this recording to the given MIDI file.
     *
     * @param file The file to write to.
     */
    public void write(File file) {
        try {
            MidiSystem.write(createSequence(), FILE_TYPE, file);
        } catch (IOException|InvalidMidiDataException e) {
            System.out.println("Could not save recording.");
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
        for(int i = 0; i < messages.size(); i++) {
            long tick = milliToTick(timestamps.get(i));
            MidiEvent event = new MidiEvent(messages.get(i), tick);
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
        return new ShortMessage(on, currentChannel, note.getNumber(), note.getVelocity());
    }

    /**
     * Creates a MIDI message for setting the pitch bend.
     *
     * @param amount the amount to set bend to with the MIDI message.
     * @return a midi mesage describing the given bend amount.
     * @throws InvalidMidiDataException if there is an error creating the message.
     */
    private ShortMessage bendMessage(int amount) throws InvalidMidiDataException {
        if(amount < 0) {
            amount = 0;
        } else if(amount > MAX_14) {
            amount = MAX_14;
        }

        // convert int into two halves of a 14-bit int
        // sourced from https://arduino.stackexchange.com/questions/18955/how-to-send-a-pitch-bend-midi-message-using-arcore
        int lowValue = amount & 0x7F;
        int highValue = amount >> 7;

        return new ShortMessage(ShortMessage.PITCH_BEND, currentChannel, lowValue, highValue);
    }

    /**
     * Converts a time in milliseconds to a time in MIDI ticks.
     * Currently based on a 30 FPS, 10 resolution time setup.
     *
     * @param millis a timestamp in milliseconds.
     * @return the MIDI tick closest to the given time in milliseconds.
     */
    private long milliToTick(long millis) {
        long ticksPerSecond = 30 * 10;
        long millisPerSecond = 1000;

        return (long) ((double) millis / millisPerSecond * ticksPerSecond);
    }

}