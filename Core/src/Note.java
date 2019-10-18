public class Note {

    private boolean on;
    private int number;
    private int velocity;

    /**
     * Creates an immutable Note object
     * @param number
     * @param velocity
     * @param on is the note on or off.
     */
    public Note(int number, int velocity, boolean on) {
        this.number = number;
        this.velocity = velocity;
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

}
