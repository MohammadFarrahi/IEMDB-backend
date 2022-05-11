package ie.iemdb.model;

import ie.iemdb.exception.ObjectNotFoundException;
import ie.iemdb.model.DTO.ActorBriefDTO;
import ie.iemdb.model.DTO.ActorDTO;
import ie.iemdb.model.DTO.MovieBriefDTO;
import ie.iemdb.repository.Retriever;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;


public class Actor {
    private Integer id;
    private String name;
    private String birthDate;
    private String nationality;
    private ArrayList<Movie> performedMovies = null;
    private String imgUrl;
    private Retriever retriever;


    public Actor ( Integer id, String name, String birthDate, String nationality, String imgUrl ) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.imgUrl = imgUrl;
    }

    public void setRetriever(Retriever retriever){
        this.retriever = retriever;
    }

    private ArrayList<Movie> getPerformedMovies() throws SQLException, ObjectNotFoundException {
        if(this.performedMovies == null)
            this.performedMovies = this.retriever.getMoviesForActor(this.id);
        return this.performedMovies;
    }

    public Integer getAge() {
        return Period.between(LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("MMMM d, yyyy")), LocalDate.now()).getYears();
    }


    public Integer getId() {
        return this.id;
    }

    public ActorDTO getDTO() throws SQLException, ObjectNotFoundException {
        var actorDTO = new ActorDTO();
        actorDTO.setId(id);
        actorDTO.setBirthDate(birthDate);
        actorDTO.setImgUrl(imgUrl);
        actorDTO.setName(name);
        actorDTO.setNationality(nationality);
        var performedMoviesDTO = new ArrayList<MovieBriefDTO>();
        getPerformedMovies().forEach(movie -> performedMoviesDTO.add(movie.getShortDTO()));
        actorDTO.setPerformedMovies(performedMoviesDTO);
        return actorDTO;
    }
    public ActorBriefDTO getBriefDTO() {
        var DTO = new ActorBriefDTO();
        DTO.setId(id);
        DTO.setAge(this.getAge());
        DTO.setImgUrl(imgUrl);
        DTO.setName(name);
        return DTO;
    }
    public Map<String, String> getDBTuple() {
        return Map.of("id", id.toString(), "name", name, "birthDate", birthDate.toString(), "nationality", nationality, "imgUrl", imgUrl);
    }

    public String getName() {
        return name;
    }
}
