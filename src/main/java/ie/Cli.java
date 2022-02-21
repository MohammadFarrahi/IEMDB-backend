package ie;


import java.util.Scanner;

public class Cli {
    private final Iemdb iemdb;
    public Cli() {
        this.iemdb = new Iemdb();
    }
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String line;

        while((line = scanner.nextLine()) != null) {
            String[] commandParts = line.split(" ", -2);
            execCommand(commandParts);
            String res = iemdb.getResponse();
            System.out.println(res);
        }
    }

    private void execCommand(String[] commandParts) throws IllegalStateException{
        String command = commandParts[0];

        switch (command) {
            case "addActor" -> System.out.println("FUCK");
            default -> throw new IllegalStateException("Unexpected value: " + command);
        }
    }
}
