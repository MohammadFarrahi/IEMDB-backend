package ie;

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
        iemdb.runTextCommand(command, data);
    }
}
