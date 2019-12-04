import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AddKeyWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane keyOut;
    private JTextField noteNum;
    private JTextField instNum;
    private JTextPane noteNumberTextPane;
    private JTextPane instrumentNumberTextPane;
    private JTextField keyNum;
    private ArrayList<int[]> noteList;

    public AddKeyWindow(ArrayList<int[]> nl) {
        noteList = nl;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
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

    private void onOK() {
        noteList.add(new int[]{Integer.parseInt(keyNum.getText()), Integer.parseInt(noteNum.getText()), Integer.parseInt(instNum.getText())});
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        AddKeyWindow dialog = new AddKeyWindow(new ArrayList<int[]>());
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
