package ca.cmpt213.a4.control;

import ca.cmpt213.a4.model.Task;
import ca.cmpt213.a4.view.TaskInfoCollector;
import com.google.gson.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * TaskManager class controls and manages the task list to correspond with the user's desired action on the menu user interface.
 * TaskManager class contains the taskList as its private field, and can add new task into this taskList, remove a task from this taskList, save taskList into Json file, and load tasks into the taskList from Json file.
 * TaskManager is an ActionListener, it will listen to the buttons from the menu graphical user interface, and then make corresponding updates for its taskList, after modifying the list it will click the button to list all tasks in order for the menu to immediately show all tasks with the new changes.
 */
public class TaskManager implements ActionListener {
    //fields
    private final ArrayList<Task> taskList;
    private JButton listAllTaskButton;

    public TaskManager()
    {
        taskList = new ArrayList<>();
        listAllTaskButton = null;
    }

    public ArrayList<Task> getTaskList()
    {
        return this.taskList;
    }

    public void setButton(JButton list_all_task_button)
    {
        this.listAllTaskButton = list_all_task_button;
    }

    public void addTask() throws IOException {
        //tell GUI to get info abt the new task
        Task newTask = TaskInfoCollector.getNewTaskIn();
        //then add the task into this.taskList
        if(newTask != null)
        {
            //fine a place to insert this new task into the taskList
            int i;
            boolean foundInsertSpot = false;
            for (i = 0; i < taskList.size() && !foundInsertSpot; i++) {
                int diff = newTask.getDueDate().compareTo(taskList.get(i).getDueDate());
                if (diff < 0) {
                    taskList.add(i, newTask);
                    foundInsertSpot = true;
                }
            }
            if (!foundInsertSpot)
                taskList.add(newTask);

            //after inserting the task, we press the list all tasks button to display the updated task list
            this.listAllTaskButton.doClick();
        }
    }

    public void removeTask(int targetIndex)
    {
        this.taskList.remove(targetIndex);
    }

    public void saveToJsonFile()
    {
        try {
            FileWriter writer = new FileWriter("./data.json");
            Gson gson = new Gson();

            writer.write("{\"taskList\": ");
            String taskAL = gson.toJson(this.taskList);
            writer.write(taskAL);
            writer.write("}");

            writer.close();
        }
        catch(IOException e)
        {
            System.out.println("Error: cannot find the file to write on!");
            e.printStackTrace();
        }
    }

    public void loadFromJsonFile()
    {
        try
        {
            File input = new File("./data.json");
            if (!input.createNewFile()) // if previous json file existed
            {
                FileReader reader = new FileReader(input);
                JsonElement fileElement = JsonParser.parseReader(reader);
                JsonObject fileObject = fileElement.getAsJsonObject();

                //extracting
                Gson gson = new Gson();
                JsonArray jsonTaskList = fileObject.get("taskList").getAsJsonArray();
                for (JsonElement taskElement : jsonTaskList)
                {
                    //get jsonObject
                    JsonObject taskJsonObject = taskElement.getAsJsonObject();
                    //extract data
                    taskList.add(gson.fromJson(taskJsonObject, Task.class));
                }
                reader.close();
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error: file not found.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Error: cannot create new file.");
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //will be invoked if some remove button is pressed
        String taskIndexAsString = e.getActionCommand();
        int taskIndex = Integer.parseInt(taskIndexAsString);
        removeTask(taskIndex);
        //reprint the task list on GUI
        this.listAllTaskButton.doClick();
    }

}
