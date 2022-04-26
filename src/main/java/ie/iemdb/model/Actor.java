package ie.iemdb.model;

import ie.iemdb.model.DTO.ActorBriefDTO;
import ie.iemdb.model.DTO.ActorDTO;
import ie.iemdb.model.DTO.MovieBriefDTO;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Actor {
    private String id;
    private String name;
    private LocalDate birthDate;
    private String nationality;
    private ArrayList<Movie> performedMovies;
    private String imgUrl;


    public Actor ( String id, String name, String birthDate, String nationality, String imgUrl ) {
        this.id = id;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        this.nationality = nationality;
        this.performedMovies = new ArrayList<>();
        this.imgUrl = imgUrl;
    }

    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    public void addToPerformedMovies(Movie movie) {
        performedMovies.add(movie);
    }

    public String getId() {
        return this.id;
    }

    public ActorDTO getDTO() {
        var actorDTO = new ActorDTO();
        actorDTO.setId(Integer.parseInt(id));
        actorDTO.setBirthDate(birthDate);
        actorDTO.setImgUrl(imgUrl);
        actorDTO.setName(name);
        actorDTO.setNationality(nationality);
        var performedMoviesDTO = new ArrayList<MovieBriefDTO>();
        performedMovies.forEach(movie -> performedMoviesDTO.add(movie.getShortDTO()));
        actorDTO.setPerformedMovies(performedMoviesDTO);
        return actorDTO;
    }
    public ActorBriefDTO getBriefDTO() {
        var DTO = new ActorBriefDTO();
        DTO.setId(Integer.parseInt(id));
        DTO.setAge(this.getAge());
        DTO.setImgUrl(imgUrl);
        DTO.setName(name);
        return DTO;
    }

}
