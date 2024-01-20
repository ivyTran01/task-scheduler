package ca.cmpt213.a4.model;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.GregorianCalendar;

/**
 * Task class represents a task that has features including name, notes, dueDate, and completion status.
 * Task class also consists of methods that return its specific field, set its attributes, and methods that return all of a task's attributes as a string for display purpose.
 */
public class Task implements ItemListener {
    private final String name;
    private final String notes;
    private final GregorianCalendar dueDate;
    private boolean completed;

    //public methods
    public Task(String name, String notes, GregorianCalendar dueDate, boolean completed) {
        this.name = name;
        this.notes = notes;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getName() {
        return this.name;
    }

    public String getNotes() {
        return this.notes;
    }

    public GregorianCalendar getDueDate()
    {
        return dueDate;
    }

    public boolean getCompletedStatus()
    {
        return completed;
    }

    public String getCompletedStatusAsString()
    {
        String result = "yes";
        if(!this.completed)
            result = "no";
        return result;
    }

    public String getDueDateAsString() {
        int userMonth = dueDate.get(GregorianCalendar.MONTH) + 1;
        //reformat some numbers
        String month = String.format("%02d", userMonth);
        String dayInMonth = String.format("%02d", dueDate.get(GregorianCalendar.DAY_OF_MONTH));
        String hour = String.format("%02d", dueDate.get(GregorianCalendar.HOUR_OF_DAY));
        String minute = String.format("%02d", dueDate.get(GregorianCalendar.MINUTE));

        String result = "";
        result = result + dueDate.get(GregorianCalendar.YEAR) + "-" + month + "-"  + dayInMonth + " " + hour + ":" + minute;

        return result;
    }

    @Override
    public String toString() {
        String result = "";
        result = result + "Task: " + this.getName() + "\n"
                + "Notes: " + this.getNotes() + "\n"
                + "Due date: " + this.getDueDateAsString() + "\n"
                + "Completed: " + this.getCompletedStatusAsString() + "\n\n";
        return result;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox completedCheckBox = (JCheckBox)e.getSource();
        this.completed = completedCheckBox.isSelected();
    }
}

