package ca.cmpt213.a4.view;

import ca.cmpt213.a4.control.TaskManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

/**
 * MenuGUI class is the graphical user interface that makes an interacting window for user to navigate and easily use the task tracker program.
 * MenuGUI has buttons for the user to click on to display all tasks, or display overdue/upcoming incomplete tasks, or to add a new task.
 * MenuGUI also contains a scrollable panel that display the tasks according to the mode the user clicks on (three modes: list all tasks, overdue, upcoming).
 */
public class MenuGUI implements ActionListener, WindowListener {
    private final JFrame menuFrame;
    private final JPanel contentPanel;
    private final JPanel topPanel;
    private final JPanel bigListPanel;
    private final JPanel bottomPanel;
    private final JButton listAllTasksButton;
    private final JButton overdueButton;
    private final JButton upcomingButton;
    private final JButton addTaskButton;
    private final TaskManager TASK_MAN;
    private final DisplayTasks taskDisplayer;

    public MenuGUI()
    {
        TASK_MAN = new TaskManager();
        TASK_MAN.loadFromJsonFile();
        taskDisplayer = new DisplayTasks();
        //set up the frame
        menuFrame = new JFrame("MY TO DO'S LIST");
        menuFrame.setSize(600, 550);
        menuFrame.setVisible(true);
        menuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        menuFrame.addWindowListener(this);

        //top panel
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));

        listAllTasksButton = new JButton("List all tasks");
        listAllTasksButton.addActionListener(this);
        listAllTasksButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //we need to give our private task manager the access to click the list all task button
        //this will help update the task list page once changes are made in the task list
        TASK_MAN.setButton(listAllTasksButton);

        overdueButton = new JButton("Overdue");
        overdueButton.addActionListener(this);
        overdueButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        upcomingButton = new JButton("Upcoming");
        upcomingButton.addActionListener(this);
        upcomingButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(listAllTasksButton);
        topPanel.add(overdueButton);
        topPanel.add(upcomingButton);

        //scroll panel
        bigListPanel = new JPanel();
        bigListPanel.setLayout(new BoxLayout(bigListPanel, BoxLayout.X_AXIS));
        taskDisplayer.listAllTasksRepaint(bigListPanel, TASK_MAN);

        //bottom panel
        addTaskButton = new JButton("Add task");
        addTaskButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addTaskButton.addActionListener(this);

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(addTaskButton);

        //add all into the big panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(Box.createRigidArea(new Dimension(10,20)));
        contentPanel.add(topPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(10,20)));
        contentPanel.add(bigListPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(10,20)));
        contentPanel.add(bottomPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(10,20)));

        menuFrame.setContentPane(contentPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //divide into different cases where different buttons is pressed
        if(e.getActionCommand().equals("List all tasks"))
        {
            taskDisplayer.listAllTasksRepaint(bigListPanel, TASK_MAN);
        }
        else if(e.getActionCommand().equals("Overdue"))
        {
            taskDisplayer.listOverdueIncompleteTasksRepaint(bigListPanel, TASK_MAN);
        }
        else if(e.getActionCommand().equals("Upcoming"))
        {
            taskDisplayer.listUpcomingIncompleteTasksRepaint(bigListPanel, TASK_MAN);
        }
        else if(e.getActionCommand().equals("Add task"))
        {
            try {
                TASK_MAN.addTask();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosing(WindowEvent e) {
        TASK_MAN.saveToJsonFile();
        menuFrame.dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
}
