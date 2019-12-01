import javax.sound.midi.*;
public class synthesizer_latency {

    public static void main(String[] args) throws MidiUnavailableException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        //Microseconds
        System.out.println("Synthesizer Latency: "+synth.getLatency());
    }

}
