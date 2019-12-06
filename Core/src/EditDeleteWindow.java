import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class EditDeleteWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JList fileList;

    public EditDeleteWindow() throws FileNotFoundException {
        Scanner s;
        PrintWriter pw;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        File resources = new File("resources");
        File temp = new File("temp");
        boolean check = temp.mkdir();
        for (File file : resources.listFiles()){
            s = new Scanner (file);
            File newFile = new File("temp\\" + file.getName());
            pw = new PrintWriter(newFile);
            while (s.hasNext()){
                pw.println(s.nextLine());
            }
            pw.close();
        }

        fileList.setListData(temp.list());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /*resources.delete();

                for (File file : temp.listFiles()){
                    try {
                        Scanner s = new Scanner(file);
                        File newFile = new File("resources\\" + file.getName());
                        PrintWriter pw = new PrintWriter(newFile);
                        while (s.hasNext()) {
                            pw.println(s.nextLine());
                        }
                        pw.close();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                temp.delete();*/
                resources.renameTo(new File("temp2"));
                temp.renameTo(new File("resources"));
                resources.delete();

                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                temp.delete();
                onCancel();
            }
        });

        buttonEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                try {
                    editLoadout(temp.list());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int offset = 0;
                for (int i : fileList.getSelectedIndices()){
                    temp.listFiles()[i-offset].delete();
                    offset++;
                }
                fileList.setListData(temp.list());
            }
        });

                // call onCancel() when cross is clicked
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                temp.delete();
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                temp.delete();
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void editLoadout(String[] temp) throws FileNotFoundException {
        for (int index : fileList.getSelectedIndices()) {
            AddLoadoutWindow ed = new AddLoadoutWindow(true, temp[index]);
            ed.pack();
            ed.setVisible(true);
            while (ed.isVisible()) {}
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        EditDeleteWindow dialog = new EditDeleteWindow();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
