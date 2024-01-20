package ca.cmpt213.a4.view;

import ca.cmpt213.a4.model.Task;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

/**
 * TaskInfoCollector class contains the static function getNewTaskIn() which receives input information about the new task from the user.
 * TaskInfoCollector class collect new task information by making an option pane for user to enter the task name, task note, and task due date.
 * TaskInfoCollector will then make a Task object using these inputted information.
 * TaskInfoCollector also has the ability to generate random task name and date whenever the user wants to add a new task.
 */
public class TaskInfoCollector {
    public static Task getNewTaskIn()
    {
        String[] taskNameNotes = generateRandomTaskInfo();
        int optionChosen = JOptionPane.OK_OPTION;
        boolean validTask = false;
        JTextField nameTextField = new JTextField(taskNameNotes[0]);
        JTextField noteTextField = new JTextField(taskNameNotes[1]);
        DateTimePicker scheduler = new DateTimePicker();
        while(!validTask && optionChosen != JOptionPane.CANCEL_OPTION)
        {
            JPanel taskInfoPanel = makeTaskInfoPanel(nameTextField, noteTextField, scheduler);
            //pop up the option pane for user to input
            optionChosen = JOptionPane.showConfirmDialog(null, taskInfoPanel, "New Task Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (optionChosen == JOptionPane.OK_OPTION)
            {
                //make sure task name and date not empty
                if(nameTextField.getText() == null || nameTextField.getText().equals(""))
                    JOptionPane.showMessageDialog(null, "Task name cannot be empty!", "Task Name Error", JOptionPane.ERROR_MESSAGE);
                else if(scheduler.getDateTimeStrict() == null)
                    JOptionPane.showMessageDialog(null, "Due date cannot be empty!", "Due Date Error", JOptionPane.ERROR_MESSAGE);
                else
                    validTask = true;
            }
        }

        if(validTask)
        {
            String task_name = nameTextField.getText();
            String task_note;
            if(noteTextField.getText() == null || noteTextField.getText().equals(""))
                task_note = "N/A";
            else
                task_note = noteTextField.getText();

            LocalDateTime localDueDateTime = scheduler.getDateTimeStrict();
            int yearDue = localDueDateTime.getYear();
            int monthDue = localDueDateTime.getMonthValue() - 1;
            int dayDue = localDueDateTime.getDayOfMonth();
            int hourDue = localDueDateTime.getHour();
            int minuteDue = localDueDateTime.getMinute();
            GregorianCalendar dueDate = new GregorianCalendar(yearDue, monthDue, dayDue, hourDue, minuteDue);

            return new Task(task_name, task_note, dueDate, false);
        }
        return null;
    }

    private static JPanel makeTaskInfoPanel(JTextField nameTextField, JTextField noteTextField, DateTimePicker scheduler)
    {
        //create panel for name input
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
        namePanel.add(new JLabel("Enter task name:    "));
        nameTextField.setMinimumSize(new Dimension(280, 25));
        nameTextField.setPreferredSize(new Dimension(280, 25));
        nameTextField.setMaximumSize(new Dimension(280, 25));
        namePanel.add(nameTextField);

        //create panel for task note input
        JPanel notePanel = new JPanel();
        notePanel.setLayout(new BoxLayout(notePanel, BoxLayout.X_AXIS));
        notePanel.add(new JLabel("Enter task note:     "));
        noteTextField.setMinimumSize(new Dimension(280, 25));
        noteTextField.setPreferredSize(new Dimension(280, 25));
        noteTextField.setMaximumSize(new Dimension(280, 25));
        notePanel.add(noteTextField);

        //create panel for date input
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.X_AXIS));
        datePanel.add(new JLabel("Enter task due date: "));
        scheduler.setSize(280, 25);
        datePanel.add(scheduler);

        //make the big panel to put into the option pane dialog
        JPanel taskInfoPanel = new JPanel();
        taskInfoPanel.setLayout(new BoxLayout(taskInfoPanel, BoxLayout.Y_AXIS));
        taskInfoPanel.add(namePanel);
        taskInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        taskInfoPanel.add(notePanel);
        taskInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        taskInfoPanel.add(datePanel);
        taskInfoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return taskInfoPanel;
    }

    private static String[] generateRandomTaskInfo()
    {
        //get the random activity from bored API into a json file
        getRandomActivityIntoJsonFile();

        //now retrieve that random activity from that json file to string
        String[] result = new String[2];
        try
        {
            File inputFile = new File("./randomActivity.json");
            FileReader reader = new FileReader(inputFile);
            JsonElement fileElem = JsonParser.parseReader(reader);
            JsonObject fileObject = fileElem.getAsJsonObject();
            String activityValue = fileObject.get("activity").getAsString();
            String type = fileObject.get("type").getAsString();
            String participants = fileObject.get("participants").getAsString();
            String price = fileObject.get("price").getAsString();

            String task_notes = "type: " + type + ", participants: " + participants + ", price: " + price;
            result[0] = activityValue;
            result[1] = task_notes;

            reader.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("ERROR: File not found.");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            System.out.println("ERROR: File cannot be closed.");
            e.printStackTrace();
        }
        return result;
    }

    private static void getRandomActivityIntoJsonFile()
    {
        try
        {
            // make the curl command
            String command = "curl -X GET http://www.boredapi.com/api/activity/";
            ProcessBuilder process_builder = new ProcessBuilder(command.split(" "));

            //Reference: https://mkyong.com/java/java-processbuilder-examples/
            Process bored_process = process_builder.start();
            InputStreamReader input_stream_reader = new InputStreamReader(bored_process.getInputStream());
            BufferedReader buffered_reader = new BufferedReader(input_stream_reader);

            FileWriter writer = new FileWriter("./randomActivity.json");
            String line;
            line = buffered_reader.readLine();
            while(line != null)
            {
                writer.write(line);
                line = buffered_reader.readLine();
            }
            writer.close();

            bored_process.waitFor();
            bored_process.destroy();
        }
        catch(IOException e)
        {
            System.out.println("Caught IOException");
            e.printStackTrace();
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
