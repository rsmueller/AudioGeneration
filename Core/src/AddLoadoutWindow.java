import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AddLoadoutWindow extends JDialog {
    String[] columnNames = {"Key Number", "Instrument", "Note Number"};
    private JPanel contentPane;
    private JPanel NameBox;
    private JPanel MouseCheck;
    private JPanel KeyInput;
    private JTextPane nameTextPane;
    private JCheckBox mouseCheck;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loadoutName;
    private JButton addKey;
    private JList keyTable;
    private JPanel BottomButtons;
    private JButton deleteKey;
    private ArrayList<int[]> noteList = new ArrayList<int[]>();


    public AddLoadoutWindow() throws FileNotFoundException{
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        updateList();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    onOK();
                } catch (FileNotFoundException ex) {

                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        addKey.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddKeyWindow keyWin = new AddKeyWindow(noteList);
                keyWin.pack();
                keyWin.setVisible(true);
                while (keyWin.isVisible()){}
                updateList();
            }
        });

        deleteKey.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int offset = 0;
                for (int i : keyTable.getSelectedIndices()) {
                    noteList.remove(i-offset);
                    offset += 1;
                }
                updateList();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() throws FileNotFoundException {
        System.out.println(loadoutName.getText());
        for (int[] value : noteList) {
            for (int i : value) {
                System.out.print(i + " ");
            }
            System.out.println();
        }

        File loadout = new File("resources/" + loadoutName.getText() + ".layout");
        PrintWriter pw = new PrintWriter(loadout);
        if (mouseCheck.isSelected()){
            pw.println("1");
        } else {
            pw.println("-1");
        }

        for (int[] ints : noteList) {
            pw.println(String.format("%d %d %d", ints[0], ints[1], ints[2]));
        }
        pw.close();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void updateList() {
        Object[] tempList = new Object[3];
        String[] listString = new String[noteList.size()];
        for (int i = 0; i < noteList.size(); i++) {
            for (int j = 0; j < noteList.get(i).length; j++) {
                tempList[j] = noteList.get(i)[j];
            }
            listString[i] = String.format("%d, %d, %d", tempList[0], tempList[1], tempList[2]);
        }

        keyTable.setListData(listString);
    }

    public static void main(String[] args) throws FileNotFoundException {
        AddLoadoutWindow dialog = new AddLoadoutWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
