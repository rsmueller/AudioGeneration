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

    private int currentTrack = 0;

    // Internal storage of notes and times
    private List<List<ShortMessage>> messages;
    private List<List<Long>> timestamps;

    private Sequencer sequencer;

    /**
     * Creates a new empty recording
     */
    Recording() {
        messages = new ArrayList<List<ShortMessage>>();
        timestamps = new ArrayList<List<Long>>();
        messages.add(new ArrayList<ShortMessage>());
        timestamps.add(new ArrayList<Long>());

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
     * Begins this recording. Future notes will be offset from the current time.
     */
    public void start() {
        startTime = Instant.now().toEpochMilli();;
    }

    /**
     * Adds the given note to the recording and creates a timestamp for it.
     *
     * @param channel The channel to add this note on.
     * @param note The note event to add to this recording.
     */
    public void addNote(int channel, Note note) {
        if(startTime != 0) { // if the recording has started
            long currentTime = Instant.now().toEpochMilli();

            try {
                ShortMessage message = noteMessage(channel, note);
                messages.get(currentTrack).add(message);
                timestamps.get(currentTrack).add(currentTime - startTime);
            } catch (InvalidMidiDataException e) {
                // Should never be reached
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds the given bend amount to the recording and creates a timestamp for it.
     *
     * @param channel The channel to set bend on.
     * @param amount The amount to set pitch bend to.
     */
    public void setBend(int channel, int amount) {
        if(startTime != 0) { // if the recording has started
            long currentTime = Instant.now().toEpochMilli();
            try {
                ShortMessage message = bendMessage(channel, amount);
                messages.get(currentTrack).add(message);
                timestamps.get(currentTrack).add(currentTime - startTime);
            } catch (InvalidMidiDataException e) {
                // Should never be reached
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds the given program/instrument change to the recording and creates a timestamp for it.
     *
     * Will replace any other program changes at time 0 if recording has not started.
     *
     * @param channel The channel to set bend on.
     * @param program The program to set the given channel to.
     */
    public void setProgram(int channel, int program) {
        if(startTime == 0) { // if the recording has not started
            List<ShortMessage> toRemove = new ArrayList<ShortMessage>();
            for(int i = 0; i < messages.get(currentTrack).size(); i++) {
                if(messages.get(currentTrack).get(i).getCommand() == ShortMessage.PROGRAM_CHANGE
                        && timestamps.get(currentTrack).get(i) == 0) {
                    toRemove.add(messages.get(currentTrack).get(i));
                }
            }

            for(ShortMessage remove : toRemove) {
                int index = messages.indexOf(remove);
                if(index != -1) {
                    messages.remove(index);
                    timestamps.remove(index);
                }
            }

            try {
                ShortMessage message = programChangeMessage(channel, program);
                messages.get(currentTrack).add(message);
                timestamps.get(currentTrack).add(0L);
            } catch (InvalidMidiDataException e) {
                // Should never be reached
                e.printStackTrace();
            }
        } else { // if the recording has started
            long currentTime = Instant.now().toEpochMilli();
            try {
                ShortMessage message = programChangeMessage(channel, program);
                messages.get(currentTrack).add(message);
                timestamps.get(currentTrack).add(currentTime - startTime);
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
        if(currentTrack < 127) { // MIDI has a max of 128 tracks
            currentTrack++;
            messages.add(new ArrayList<ShortMessage>());
            timestamps.add(new ArrayList<Long>());
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
     * Clears this recording, removing all data.
     */
    public void clear() {
        startTime = 0;
        timestamps.clear();
        messages.clear();

        currentTrack = 0;
        messages.add(new ArrayList<ShortMessage>());
        timestamps.add(new ArrayList<Long>());
    }

    /**
     * Converts the recording to a MIDI sequence.
     *
     * @return a MIDI Sequence representing this recording.
     * @throws InvalidMidiDataException if there is an error creating the MIDI sequence.
     */
    private Sequence createSequence() throws InvalidMidiDataException {
        Sequence sequence = new Sequence(Sequence.SMPTE_30, 10);
        for(int trackNum = 0; trackNum < messages.size(); trackNum++) {
            Track track = sequence.createTrack();
            for (int i = 0; i < messages.get(trackNum).size(); i++) {
                long tick = milliToTick(timestamps.get(trackNum).get(i));
                MidiEvent event = new MidiEvent(messages.get(trackNum).get(i), tick);
                track.add(event);
            }
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
    private ShortMessage noteMessage(int channel, Note note) throws InvalidMidiDataException {
        int on = note.isOn() ? ShortMessage.NOTE_ON : ShortMessage.NOTE_OFF;
        return new ShortMessage(on, channel, note.getNumber(), note.getVelocity());
    }

    /**
     * Creates a MIDI message for setting the pitch bend.
     *
     * @param amount the amount to set bend to with the MIDI message.
     * @return a midi mesage describing the given bend amount.
     * @throws InvalidMidiDataException if there is an error creating the message.
     */
    private ShortMessage bendMessage(int channel, int amount) throws InvalidMidiDataException {
        if(amount < 0) {
            amount = 0;
        } else if(amount > MAX_14) {
            amount = MAX_14;
        }

        // convert int into two halves of a 14-bit int
        // sourced from https://arduino.stackexchange.com/questions/18955/how-to-send-a-pitch-bend-midi-message-using-arcore
        int lowValue = amount & 0x7F;
        int highValue = amount >> 7;

        return new ShortMessage(ShortMessage.PITCH_BEND, channel, lowValue, highValue);
    }

    /**
     * Creates a MIDI message for setting the program.
     *
     * @param program the program to set the given channel to.
     * @return a midi mesage describing the program change.
     * @throws InvalidMidiDataException if there is an error creating the message.
     */
    private ShortMessage programChangeMessage(int channel, int program) throws InvalidMidiDataException {
        return new ShortMessage(ShortMessage.PROGRAM_CHANGE, channel, program, 0);
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