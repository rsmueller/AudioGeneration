import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The loadout stores what each key of the keyboard does,
 * i.e what instrument and the note on that instrument.
 */
public class Loadout {

    private Dictionary<Integer, Integer[]> notes = new Hashtable<>();
    public Integer[] typesOfInstruments;
    private InstrumentHandler mouseInstrument;

    public Loadout(String loadout){
        this(new File("C:\\Users\\thatg\\Documents\\GitHub\\AudioGeneration\\resources\\piano.layout"));
    }

    public Loadout(File loadout){
        try {
            ArrayList<Integer> instruments = new ArrayList<Integer>();
            Scanner sc = new Scanner(loadout);
            typesOfInstruments = new Integer[instruments.size()];
            int mouseInstrCode = Integer.parseInt(sc.nextLine());
            mouseInstrument = (InstrumentHandler) ((Class)LoadoutManager.getInstrumentClass(mouseInstrCode)).getConstructor(new Class[0]).newInstance();
            while (sc.hasNextLine()){
                String line = sc.nextLine();
                int space = line.indexOf(" ");
                int space2 = line.lastIndexOf(" ");
                int keyCode = Integer.parseInt(line.substring(0, space));
                int note = Integer.parseInt(line.substring(space+1, space2));
                int instr = Integer.parseInt(line.substring(space2+1));
                if (! instruments.contains(instr)){
                    instruments.add(instr);
                }
                //key is KeyCode, 2nd is the note and instrument it is on
                notes.put(keyCode, new Integer[]{note, instr});
            }
            instruments.toArray(typesOfInstruments);
        }catch(FileNotFoundException e){
            System.out.println("Error, layout file not found.");
            System.out.println(e.getMessage());
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }catch(NumberFormatException e){
            System.out.println(".layout file not formatted correctly, crashed");
            System.exit(2);
        } catch (InstantiationException e) {
            System.out.println("Could not find instrument class");
            e.printStackTrace();
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getNote(int keyCode){
        return notes.get(keyCode)[0];
    }

    public int getInstrument(int keyCode){
        return notes.get(keyCode)[1];
    }

    public InstrumentHandler getMouseInstrument(){return mouseInstrument;}

    //Test it works
    public static void main(String[] args) {
        File file = new File("C:\\Users\\thatg\\Documents\\GitHub\\AudioGeneration\\resources\\piano.layout");
        Loadout l = new Loadout(file);
        System.out.println(l.getNote(10)); //should be 59
    }
}
