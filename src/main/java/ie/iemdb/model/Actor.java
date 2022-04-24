package ie.iemdb.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;


public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private ArrayList<String> performedMovies;


    private Actor ( String id, String name, String birthDate, String nationality) {
        this.id = id;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate);
        this.nationality = nationality;
        this.performedMovies = new ArrayList<>();
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    public void addToPerformedMovies(String id) {
        performedMovies.add(id);
    }

    public String getId() {
        return this.id;
    }
}
