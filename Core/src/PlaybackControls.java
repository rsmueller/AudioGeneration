import java.awt.event.KeyEvent;
import java.io.File;

/**
 * A (currently) keyboard-based controller for playing back recordings.
 *
 * Current controls are:
 * Press F1 to playback the recording
 * Press F2 to save the recording to output.midi
 * Press F3 to begin recording a new layer
 */
public class PlaybackControls implements KeyboardListener {

    private  SoundSynthesizer synth;

    /**
     * Creates new PlaybackControls for the given synth.
     *
     * @param synth The synth to control playback of.
     */
    PlaybackControls(SoundSynthesizer synth) {
        this.synth = synth;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        // Play the recording when F1 is pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_F1) {
            System.out.println("playback time");
            synth.getRecording().play();
        }

        // Save the recording to a file when F2 is pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_F2) {
            synth.getRecording().write(new File("output.midi"));
        }

        // Begin recording a new layer when F3 is pressed
        if(keyEvent.getKeyCode() == KeyEvent.VK_F3) {
            try {
                synth.getRecording().play();
                synth.getRecording().beginLayer();
            } catch (Exception e) {
                // Reached when too many layers are added
                // Should be prevented or displayed to user
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {}

}
