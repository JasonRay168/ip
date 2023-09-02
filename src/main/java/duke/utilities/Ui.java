package duke.utilities;

import java.util.Scanner;

import duke.exceptions.DukeException;
import duke.exceptions.EmptyListException;
import duke.exceptions.InvalidCommandException;
import duke.exceptions.InvalidCommandSyntaxException;
import duke.exceptions.InvalidTaskTimeException;
import duke.exceptions.MissingTaskDescriptionException;
import duke.exceptions.MissingTaskNameException;
import duke.exceptions.MissingTaskNumberException;

/**
 * Handles user interaction
 */
public class Ui {

    /** Variable to detect user input */
    private Scanner sc = new Scanner(System.in);

    /** Variable to show horizontal lines */
    public static final String LINE_BREAK = ("--------------------------------------------------"
            + "---------------------------------");

    /**
     * Prints greetings to the user interface
     */
    public void greet() {
        System.out.println("\n");
        System.out.println(LINE_BREAK);
        System.out.println("Welcome. My name is Duke");
        System.out.println("What will you do today?");
    }

    /**
     * Allows users to type in their inputs
     * 
     * @return Input as String
     */
    public String startInputSession() {
        System.out.println(LINE_BREAK);
        System.out.println("\n");
        String input = sc.nextLine().trim();
        System.out.println(LINE_BREAK);
        return input;
    }

    /**
     * Handles the various cases of user inputs
     *
     * @param tasks TaskList object that contains the list
     * @param input Input object that contains parsed user input
     * @param parser Parser used to parse user inputs
     * @return True or false signifying breaking or continuing the loop
     */
    public boolean handleInput(TaskList tasks, Input input, Parser parser) {
        boolean endSession = true;
        try {
            String command = input.getCommand();
            String fullInput = input.getFullInput();
            int inputLength = input.getLength();
            if (command.equals("list")) {
                if (inputLength > 1) {
                    throw new InvalidCommandSyntaxException("'list' command must not be followed by anything");
                }
                if (tasks.getSize() == 0) {
                    System.out.println("List is empty");
                } else {
                    for (int i = 1; i < tasks.getSize() + 1; i++) {
                        Task current = tasks.getTask(i - 1);
                        System.out.println(i + ". " + current.convertToString());
                    }
                }
            } else if (command.equals("mark")
                    || command.equals("unmark")
                    || command.equals("delete")) {
                if (inputLength == 1) {
                    throw new MissingTaskNumberException("Task number cannot be empty");
                } else if (tasks.getSize() < 1) {
                    throw new EmptyListException("List is currently empty");
                }
                switch (command) {
                case "mark":
                    tasks.manipulateTasks(fullInput, "mark", 5);
                    break;
                case "unmark":
                    tasks.manipulateTasks(fullInput, "unmark", 7);
                    break;
                case "delete":
                    tasks.manipulateTasks(fullInput, "delete", 7);
                    break;
                default:
                    break;
                }
            } else if (command.equals("todo")) {
                if (inputLength <= 1) {
                    throw new MissingTaskDescriptionException("Todo task description cannot be empty");
                }

                String taskName = fullInput.substring(5);
                tasks.addToDo(taskName);
            } else if (command.equals("deadline")) {
                if (inputLength <= 1) {
                    throw new MissingTaskDescriptionException("Deadline task description cannot be empty");
                }
                String[] taskDesc = fullInput.substring(9).split("/by");
                if (taskDesc.length < 2) {
                    throw new InvalidTaskTimeException("Deadline task must have exactly one /by deadline");
                }
                String taskName = taskDesc[0].trim();
                if (taskName.length() == 0) {
                    throw new MissingTaskNameException("Deadline task name cannot be empty");
                }
                String strDeadline = taskDesc[1].trim();
                String deadline = parser.formatDate(strDeadline);
                if (deadline.equals("Invalid date")) {
                    return endSession;
                }
                tasks.addDeadline(taskName, deadline);
            } else if (command.equals("event")) {
                if (inputLength <= 1) {
                    throw new MissingTaskDescriptionException("Event task description cannot be empty");
                }
                String[] taskDesc = fullInput.substring(6).split("/from");
                if (taskDesc.length != 2) {
                    throw new InvalidTaskTimeException(
                            "Event task must have exactly one /from and one /to times, in that order");
                }
                String taskName = taskDesc[0].trim();
                String[] fromAndTo = taskDesc[1].split("/to");
                if (fromAndTo.length != 2) {
                    throw new InvalidTaskTimeException(
                            "Event task must have exactly one /from and one /to times, in that order");
                }
                if (taskName.length() == 0) {
                    throw new MissingTaskNameException("Event task name cannot be empty");
                }
                String strStart = fromAndTo[0].trim();
                String strEnd = fromAndTo[1].trim();
                String start = parser.formatDate(strStart);
                if (start.equals("Invalid date")) {
                    return endSession;
                }
                String end = parser.formatDate(strEnd);
                if (end.equals("Invalid date")) {
                    return endSession;
                }
                tasks.addEvent(taskName, start, end);
            } else if (command.equals("bye")) {
                if (inputLength > 1) {
                    throw new InvalidCommandSyntaxException("'bye' command must not be followed by anything");
                }
                System.out.println("I hope you enjoy my service. Thank you and goodbye");
                System.out.println(LINE_BREAK);
                endSession = false;
            } else {
                throw new InvalidCommandException("No such command exists, please try again");
            }
        } catch (DukeException e) {
            System.out.println("!ERROR! " + e);
        }
        return endSession;
    }
}
