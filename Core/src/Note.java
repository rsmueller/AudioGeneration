public class Note {

    private int number;
    private int velocity;
    private int instrument;
    private boolean on;

    /**
     * Creates an immutable Note object
     * @param number
     * @param velocity
     * @param on is the note on or off.
     */
    public Note(int number, int velocity, int instrument, boolean on) {
        this.number = number;
        this.velocity = velocity;
        this.instrument = instrument;
        this.on = on;
    }

    public boolean isOn() {
        return on;
    }

    public int getNumber() {
        return number;
    }

    public int getVelocity() {
        return velocity;
    }

    public int getInstrument() {
        return instrument;
    }

}
