package gui;

import admanager.axis.auth.Main_class;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * SwingFileChooserDemo.java is a 1.4 application that uses these files:
 * images/Open16.gif images/Save16.gif
 */
public class Ad_Home extends JPanel implements ActionListener {
    static private final String newline = "\n";

    JButton openButton, saveButton;

    JTextArea log;

    JFileChooser fc;
    private JPanel panel1;
    private JTextArea textArea1;

    public Ad_Home() {
        super(new BorderLayout());

        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5, 5);
        log.setMargin(new Insets(5, 5, 5, 5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        //Create a file chooser
        fc = new JFileChooser();

        //Uncomment one of the following lines to try a different
        //file selection mode. The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown). The second allows both files and directories
        //to be selected. If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        //Create the open button. We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);

        //Create the save button. We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...");
        saveButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(Ad_Home.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.append("Opening: " + file.getName() + "." + newline);
                log.append("Please do not open another file, the code is running in the back ground" + newline);
                log.append("You will be prompted when it has finished" + newline);
                Main_class.file = file.getAbsolutePath();
                String extension = "";
                int ir = file.getName().lastIndexOf('.');
                if(ir>0){
                    extension = file.getName().substring(ir+1);
                }
                if(extension.equals("txt") || extension.equals("csv")){
                    log.append(newline);
//                    JOptionPane.showMessageDialog(null, "Step 1");
                    Main_class.main_main();
//                    JOptionPane.showMessageDialog(null, "Step 2");
                    if(Main_class.result.length > 0) {
                        for (int i = 0; i < Main_class.result.length; i++) {

                            if(Main_class.result[i].equals("There is something wrong with your file")){
                                JOptionPane.showMessageDialog(null, "There is something wrong with your file");
                                log.append("Please try again" + newline +newline);
                            }
                            else{
                                log.append(Main_class.result[i] + newline);
                            }

                        }
                    }
                    JOptionPane.showMessageDialog(null, "Done");
                    log.append(newline);
                }

                else{
                    JOptionPane.showMessageDialog(null, "Wrong file extension");
                    log.append("Please input the correct file" + newline +newline);
                }


            } else {
                log.append("Open command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(Ad_Home.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                log.append("Saving: " + file.getName() + "." + newline);
            } else {
                log.append("Save command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
        }
    }



    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        JFrame frame = new JFrame("SwingFileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new Ad_Home();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

}
