import java.awt.event.KeyEvent;

/**
 * A (currently) keyboard-based controller for playing back recordings.
 *
 * Current controls are to press F1 to play back the recorded music.
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
            synth.getRecording().play();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {}

}
