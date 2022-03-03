package ie;

import ie.exception.CustomException;

public class App {
    public static void main(String[] args) {
        Iemdb iemdbApp = new Iemdb();
        try {
            iemdbApp.fetchData();
            iemdbApp.startServer();
        } catch (CustomException e) {
            e.printStackTrace();
        }

    }
}
