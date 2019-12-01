import java.awt.event.*;
import java.io.File;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Loadout Manager handles loading from resources directory
 * as well as saving custom ones to resources.
 */
public class LoadoutManager implements KeyboardListener, MouseMotionListener, MouseWheelListener {

    private Loadout currentLoadout;
    private SoundSynthesizer ss;


    private static Map<Integer, Class> instrumentCodes = new HashMap<Integer, Class>() {{
        put(1, Piano.class);
        put(2, Guitar.class);
        //etc
    }};


    //Has to update ss when the loadout is changed.
    public LoadoutManager(SoundSynthesizer ss){
        this.ss = ss;
    }

    /**
     * Each instrument handler implementer has a designated code, hence instrumentCodes
     * @param code for the instrument handler
     * @return the Class obj of that instrument handler
     */
    public static Class getInstrumentClass(int code){
        return instrumentCodes.get(code);
    }

    /**
     * A reverse lookup from the Hashmap,
     * if there is a data struct makes for a two way lookup (i.e one can find the other easily)
     * PLEASE replace if that is the case.
     * @param instrClass Class obj you want to find the code for.
     * @return Code for instrument handler
     */
    public static int getInstrumentCode(Class instrClass){
        for (int code : instrumentCodes.keySet()){
            if (instrumentCodes.get(code).equals(instrClass))
                return code;
        }
        System.out.println("Instrument not in instrumentCodes, fatal error");
        System.exit(3);
        return -1;
    }

    private void setCurrentLoadout(Loadout loadout){
        currentLoadout = loadout;
        ss.clearInstruments();
        InstrumentHandler mouseInstr = loadout.getMouseInstrument();
        if (mouseInstr != null) {
            ss.setMouseInstrument(mouseInstr);
            mouseInstr.setSynth(ss);
        }
        for (Integer instr : loadout.typesOfInstruments) {
            System.out.println("Adding instrument "+instr);
            ss.addInstrument(instr);
        }
    }

    public void setToDefault(){
        File layoutFile = new File("resources\\guitar.layout");
        Loadout def = new Loadout(layoutFile);
        setCurrentLoadout(def);
    }

    public int getNote(int keyCode){
         return currentLoadout.getNote(keyCode);
    }

    public int getInstrument(int keyCode){
        return currentLoadout.getInstrument(keyCode);
    }

    public InstrumentHandler getMouseInstrument(){
        return currentLoadout.getMouseInstrument();
    }

    // ------------------Call input methods on loaded instruments-----------------------

    @Override
    public void keyPressed(KeyEvent e) {
        int instrument = getInstrument(e.getKeyCode());
        int noteNum = getNote(e.getKeyCode());
        Note note = new Note(noteNum, 100, instrument, true);
        ss.playNote(note);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int instrument = getInstrument(e.getKeyCode());
        int noteNum = getNote(e.getKeyCode());
        Note note = new Note(noteNum, 100, instrument, false);
        ss.playNote(note);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (getMouseInstrument() != null)
            getMouseInstrument().mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (getMouseInstrument() != null)
            getMouseInstrument().mouseMoved(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (getMouseInstrument() != null)
            getMouseInstrument().mouseWheelMoved(e);
    }
    // ------------------End of call input methods on loaded instruments-----------------------

}
