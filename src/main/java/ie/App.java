package ie;
//import java.time.LocalDate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.DateTimeException;

public class App {
    public static void main(String[] args) {
        Cli cli = new Cli();
        cli.run();

    }
}