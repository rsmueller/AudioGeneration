import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 * Loadout Manager handles loading from resources directory
 * as well as saving custom ones to resources.
 */
public class LoadoutManager {

    private Loadout currentLoadout;
    private SoundSynthesizer ss;


    private static Map<Integer, Class> instrumentCodes = new HashMap<Integer, Class>() {{
        put(1, Piano.class);
        //put(2, Guitar.class);
        //etc
    }};


    //Has to update ss when the loadout is changed.
    public LoadoutManager(SoundSynthesizer ss){
        this.ss = ss;
    }

    public static Class getInstrumentClass(int code){
        return instrumentCodes.get(code);
    }

    void setCurrentLoadout(Loadout loadout){
        currentLoadout = loadout;
        ss.clearInstruments();
        for (Integer instr : loadout.typesOfInstruments)
            ss.addInstrument(instr);
    }

    public int getNote(int keyCode){
         return currentLoadout.getNote(keyCode);
    }

    public int getInstrument(int keyCode){
        return currentLoadout.getInstrument(keyCode);
    }
}
