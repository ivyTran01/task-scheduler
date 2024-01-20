package ca.cmpt213.a4;

import ca.cmpt213.a4.view.MenuGUI;

import javax.swing.*;

/**
 * Main class makes a safe thread out of MenuGUI() to run the task-tracker program
 */
public class Main {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuGUI();
            }
        });
    }
}
