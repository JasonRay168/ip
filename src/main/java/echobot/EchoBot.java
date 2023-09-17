package echobot;

import java.io.File;

import echobot.utilities.Input;
import echobot.utilities.Parser;
import echobot.utilities.Storage;
import echobot.utilities.TaskList;
import echobot.utilities.Ui;

/**
 * The main class for EchoBot Chatbot
 */
public class EchoBot {

    /** Variable to show if EchoBot is running or not */
    private static boolean isRunning = false;

    /** File path to the tasks.txt */
    private static final String FILE_PATH = "./tasks.txt";

    /** File path to the previous.txt */
    private static final String PREVIOUS_PATH = "./previous.txt";

    /** Variable to store task list */
    private Storage currentStorage;

    /** Variable to store previous task list */
    private Storage previousStorage;

    /** Variable to handle list of tasks operations */
    private TaskList tasks;

    /** Variable to handle user interactions */
    private Ui ui;

    /** Variable to handle user inputs */
    private Parser parser;

    /**
     * Creates a new instance of EchoBot chatbot
     */
    public EchoBot() {
        ui = new Ui();
        parser = new Parser();
        currentStorage = new Storage(FILE_PATH);
        previousStorage = new Storage(PREVIOUS_PATH);
        if (currentStorage.fileExists()) {
            tasks = new TaskList(currentStorage.loadTasksData());
        } else {
            tasks = new TaskList();
        }
        File txtFile = new File(FILE_PATH);
        assert txtFile.exists();
    }

    /**
     * Processes and shows response based on the user input
     *
     * @param input Input typed in by user
     * @return Output made by the chatbot following the user input
     */
    public String getResponse(String input) {
        Input parsedInput = parser.parse(input);
        String output = parser.handleInput(tasks, parsedInput, ui, currentStorage, previousStorage);
        File txtFile = new File(FILE_PATH);
        assert txtFile.exists();
        return output;
    }

    /**
     * Starts the current session
     */
    public static void startBot() {
        isRunning = true;
    }

    /** Ends the current session */
    public static void stopBot() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
