package ie;
import ie.types.Command;

import java.util.Scanner;

public class Cli {
    private final Iemdb iemdb;
    private final Scanner terminal;
    public Cli() {
        this.iemdb = new Iemdb();
        this.terminal = new Scanner(System.in);
    }
    public void run() {
        String line;
        while((line = terminal.nextLine()) != null) {
            String[] commandParts = line.split(" ", 2);
            execCommand(commandParts);
            String res = iemdb.getResponse();
            System.out.println(res);
        }
    }

    private void execCommand(String[] commandParts) throws IllegalStateException {
        String command = commandParts[0];
        String data = commandParts.length == 1 ? "" : commandParts[1];

        switch (command) {
            case "addUser" -> iemdb.runCommand(Command.ADD_USER, data);
            case "addMovie" -> iemdb.runCommand(Command.ADD_MOVIE, data);
            case "addActor" -> iemdb.runCommand(Command.ADD_ACTOR, data);
            case "addComment" -> iemdb.runCommand(Command.ADD_COMMENT, data);
            case "rateMovie" -> iemdb.runCommand(Command.RATE_MOVIE, data);
            default -> iemdb.runCommand(Command.INVALID_COMMAND, null);
        }
    }
}
