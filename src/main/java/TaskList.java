import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Contains the task list and its operations
 */
public class TaskList {

    /** Variable to store the tasks */
    private ArrayList<Task> list = new ArrayList<>();

    public TaskList(ArrayList<Task> list) {
        this.list = list;
    }

    public TaskList() {
        this.list = new ArrayList<>();
    }

    /**
     * Manipulates the data of existing tasks
     * Includes marking, unmarking, and deleting tasks
     * 
     * @param command Command to be executed
     * @param list List of tasks where task data are to be changed
     * @param beginIndex Beginning index of the command description excluding the command itself
     * @throws InvalidTaskNumberException
     */
    public void manipulateTasks(String fullInput, String command, int beginIndex) throws InvalidTaskNumberException {
        int taskNum = Integer.parseInt(fullInput.substring(beginIndex));
        if (taskNum > this.getSize() || taskNum < 1) {
            throw new InvalidTaskNumberException("Task number is out of bounds, please try again");
        }
        switch (command) {
        case "mark":
            this.getTask(taskNum - 1).markAsDone();
            break;
        case "unmark":
            this.getTask(taskNum - 1).markAsNotDone();
            break;
        case "delete":
            this.deleteTask(this.getTask(taskNum - 1));
            break;
        }
    }

    public void addToDo(String taskName) {
        ToDo task = new ToDo(taskName);
        this.list.add(task);
        System.out.println("You have added a task:");
        System.out.println("\t[T][ ] " + taskName);
        System.out.println("There are now " + list.size() + " tasks in the list");
    }
    
    public void addDeadline(String taskName, String deadline) {
        Deadline task = new Deadline(taskName, deadline);
        this.list.add(task);
        System.out.println("You have added a task:");
        System.out.println("\t[D][ ] " + taskName + " (by: "+ deadline + ")");
        System.out.println("There are now " + this.getSize() + " tasks in the list");
    }

    public void addEvent(String taskName, String start, String end) {
        Event task = new Event(taskName, start, end);
        this.list.add(task);
        System.out.println("You have added a task:");
        System.out.println("\t[E][ ] " + taskName + " (from: "+ start + " to: " + end + ")");
        System.out.println("There are now " + list.size() + " tasks in the list");
    }

    /**
     * Deletes a task from the specified list
     * 
     * @param task Task object to be deleted
     * @param list List of tasks
     */
    public void deleteTask(Task task) {
        System.out.println("You have deleted a task:");
        System.out.println("\t" + task.convertToString());
        this.list.remove(task);
        System.out.println("There are now " + this.getSize() + " tasks in the list");
    }

    /**
     * Parses and formats the date input into another format
     * 
     * @param strDate Date in String format
     * @return Date in "MMM dd yyyy" format
     * @throws DateTimeParseException
     */
    public String formatDate(String strDate) throws DateTimeParseException {
        LocalDate parseDate = LocalDate.parse(strDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return parseDate.format(formatter);
    }

    public void overwriteTasksData(Storage storage) throws FileNotFoundException, IOException {
        storage.overwriteTasksData(this.list);
    }

    public Task getTask(int index) {
        return this.list.get(index);
    }

    public int getSize() {
        return this.list.size();
    }
}
