package ie;
//import java.time.LocalDate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.DateTimeException;

public class App {
    public static void main(String[] args) {
//        Cli cli = new Cli();
//        cli.run();
//        LocalDate d = LocalDate.parse("2011-13-20");
//        System.out.println(d);
        String test1 = "{\"id\": \"123457896\",\"nationality\": \"UK\",\"name\": \"test\",\"birthDate\":\"2022-02-02\"}";
        String test2 = "{\"id\": \"123457896\",\"name\": \"test\"}";
        String test3 = "{\"id\": \"123457896\",\"nationality\": \"UK\",\"name\": \"test\",\"birthDate\":\"2022-02-02\", \"extra\":\"dummy\"}";
        String test4 = "{\"id\": \"123457896\",\"nationality\": \"UK\",\"name\": \"test\",\"birthDate\":\"2022-02\"}";
        String test5 = "{\"id\": \"123457896\",\"nationality\": \"UK\",\"name\": \"test\",\"birthDate\":\"2022-02\"";
        try {
            Actor actor1 = new ObjectMapper().readValue(test1, Actor.class);
            System.out.println(actor1);
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        try {
            Actor actor2 = new ObjectMapper().readValue(test2, Actor.class);
            System.out.println(actor2);
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        try {
            Actor actor3 = new ObjectMapper().readValue(test3, Actor.class);
            System.out.println(actor3);
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        try {
            Actor actor4 = new ObjectMapper().readValue(test4, Actor.class);
            System.out.println(actor4);
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        try {
            Actor actor5 = new ObjectMapper().readValue(test5, Actor.class);
            System.out.println(actor5);
        } catch (JsonMappingException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeException e) {
            System.out.println(e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
    }
}