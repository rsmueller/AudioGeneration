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

    public static int getInstrumentCode(Class instrClass){
        for (int code : instrumentCodes.keySet()){
            if (instrumentCodes.get(code).equals(instrClass))
                return code;
        }
        System.out.println("Instrument not in instrumentCodes, fatal error");
        System.exit(3);
        return -1;
    }

    void setCurrentLoadout(Loadout loadout){
        currentLoadout = loadout;
        ss.clearInstruments();
        ss.setMouseInstrument(loadout.getMouseInstrument());
        loadout.getMouseInstrument().setSynth(ss);
        for (Integer instr : loadout.typesOfInstruments)
            ss.addInstrument(instr);
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
}
