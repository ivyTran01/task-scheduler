package ca.cmpt213.a4.view;

import ca.cmpt213.a4.control.TaskManager;
import ca.cmpt213.a4.model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * DisplayTasks class is used for printing the task list on to the menu graphical interface.
 * DisplayTasks class has three display functions that can print all tasks, or print overdue incomplete tasks, or print upcoming incomplete tasks onto the menu GUI.
 */
public class DisplayTasks {
    public DisplayTasks()
    {
        //do nothing
    }

    public void listAllTasksRepaint(JPanel bigListPanel, TaskManager task_man)
    {
        bigListPanel.removeAll(); //clear all components in this panel

        ArrayList<Task> taskList = task_man.getTaskList();
        //remake the new small list panel inside (that actually stores the task list)
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        if(taskList.isEmpty())
        {
            JLabel listEmptyLabel = new JLabel("No tasks to show.");
            listEmptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskListPanel.add(listEmptyLabel);
        }
        else
        {
            for(int i=0; i < taskList.size(); i++)
            {
                JPanel singleTaskPanel = new JPanel();
                singleTaskPanel.setMinimumSize(new Dimension(500, 120));
                singleTaskPanel.setPreferredSize(new Dimension(500, 120));
                singleTaskPanel.setMaximumSize(new Dimension(500, 120));
                singleTaskPanel.setLayout(new BoxLayout(singleTaskPanel, BoxLayout.Y_AXIS));
                singleTaskPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                singleTaskPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel taskNumLabel = new JLabel();
                taskNumLabel.setText(" Task #" + (i+1));
                JLabel nameLabel = new JLabel();
                nameLabel.setText(" Task name: " + taskList.get(i).getName());
                JLabel noteLabel = new JLabel();
                noteLabel.setText(" Task note: " + taskList.get(i).getNotes());
                JLabel dateLabel = new JLabel();
                dateLabel.setText(" Due date: " + taskList.get(i).getDueDateAsString());

                JCheckBox completedCheckBox = new JCheckBox("Completed");
                completedCheckBox.addItemListener(taskList.get(i));
                completedCheckBox.setSelected(taskList.get(i).getCompletedStatus());
                completedCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);

                JButton removeButton = new JButton("Remove Task");
                removeButton.setActionCommand(String.valueOf(i));
                removeButton.addActionListener(task_man);
                removeButton.setAlignmentX(Component.LEFT_ALIGNMENT);

                //set up the panel for this current task
                singleTaskPanel.add(taskNumLabel);
                singleTaskPanel.add(nameLabel);
                singleTaskPanel.add(noteLabel);
                singleTaskPanel.add(dateLabel);
                singleTaskPanel.add(completedCheckBox);
                singleTaskPanel.add(removeButton);

                //add that task panel into the panel for the whole task list
                taskListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                taskListPanel.add(singleTaskPanel);
            }
        }

        //make a scrollable panel for this task list panel
        JScrollPane scrollableTaskList = new JScrollPane(taskListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //repaint the big list panel
        bigListPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        bigListPanel.add(scrollableTaskList);
        bigListPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        bigListPanel.revalidate();
        bigListPanel.repaint();
    }

    public void listOverdueIncompleteTasksRepaint(JPanel bigPanel, TaskManager task_man)
    {
        bigPanel.removeAll(); //clear all components in this panel

        ArrayList<Task> taskList = task_man.getTaskList();
        //remake the new small list panel inside (that actually stores the task list)
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        if(taskList.isEmpty())
        {
            JLabel listEmptyLabel = new JLabel("No overdue incomplete tasks.");
            listEmptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskListPanel.add(listEmptyLabel);
        }
        else
        {
            GregorianCalendar currentTime = new GregorianCalendar();
            int counter = 0;
            int diff = taskList.get(0).getDueDate().compareTo(currentTime);
            for (int i = 0; (i < taskList.size()) && (diff <= 0); i++) {
                if (!taskList.get(i).getCompletedStatus()) {
                    counter++;
                    JPanel single_task_panel = makeSingleTaskPanel(counter, taskList.get(i));
                    //add that task panel into the panel for the whole task list
                    taskListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                    taskListPanel.add(single_task_panel);
                }
                if (i <= taskList.size() - 2)
                    diff = taskList.get(i + 1).getDueDate().compareTo(currentTime);
            }
            if (counter == 0) {
                JLabel listEmptyLabel = new JLabel("No overdue incomplete tasks.");
                listEmptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                taskListPanel.add(listEmptyLabel);
            }
        }

        //make a scrollable panel for this task list panel
        JScrollPane scrollableTaskList = new JScrollPane(taskListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //repaint the big list panel
        bigPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        bigPanel.add(scrollableTaskList);
        bigPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        bigPanel.revalidate();
        bigPanel.repaint();
    }

    public void listUpcomingIncompleteTasksRepaint(JPanel bigPanel, TaskManager task_man)
    {
        bigPanel.removeAll(); //clear all components in this panel

        ArrayList<Task> taskList = task_man.getTaskList();
        //remake the new small list panel inside (that actually stores the task list)
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));

        //imprint the tasks on to the task list panel
        GregorianCalendar currentTime = new GregorianCalendar();
        int diff;
        int counter = 0;
        for (Task task : taskList)
        {
            diff = task.getDueDate().compareTo(currentTime);
            if (diff > 0 && !task.getCompletedStatus())
            {
                counter++;
                JPanel single_task_panel = makeSingleTaskPanel(counter, task);
                //add that task panel into the panel for the whole task list
                taskListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
                taskListPanel.add(single_task_panel);
            }
        }
        if (counter == 0)
        {
            JLabel listEmptyLabel = new JLabel("No upcoming incomplete tasks.");
            listEmptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            taskListPanel.add(listEmptyLabel);
        }

        //make a scrollable panel for this task list panel
        JScrollPane scrollableTaskList = new JScrollPane(taskListPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //repaint the big list panel
        bigPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        bigPanel.add(scrollableTaskList);
        bigPanel.add(Box.createRigidArea(new Dimension(20, 0)));

        bigPanel.revalidate();
        bigPanel.repaint();
    }

    private static JPanel makeSingleTaskPanel(int taskCounter, Task task)
    {
        JPanel singleTaskPanel = new JPanel();
        singleTaskPanel.setMinimumSize(new Dimension(500, 70));
        singleTaskPanel.setPreferredSize(new Dimension(500, 70));
        singleTaskPanel.setMaximumSize(new Dimension(500, 70));
        singleTaskPanel.setLayout(new BoxLayout(singleTaskPanel, BoxLayout.Y_AXIS));
        singleTaskPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        singleTaskPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel taskNumLabel = new JLabel();
        taskNumLabel.setText(" Task #" + taskCounter);
        JLabel nameLabel = new JLabel();
        nameLabel.setText(" Task name: " + task.getName());
        JLabel noteLabel = new JLabel();
        noteLabel.setText(" Task note: " + task.getNotes());
        JLabel dateLabel = new JLabel();
        dateLabel.setText(" Due date: " + task.getDueDateAsString());

        //set up the panel for this current task
        singleTaskPanel.add(taskNumLabel);
        singleTaskPanel.add(nameLabel);
        singleTaskPanel.add(noteLabel);
        singleTaskPanel.add(dateLabel);

        return singleTaskPanel;
    }
}
