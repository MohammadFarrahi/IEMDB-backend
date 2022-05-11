package ie.iemdb.model.DTO;

import java.util.ArrayList;

public class ActorDTO {
    private int id;
    private String name;
    private String birthDate;
    private String nationality;
    private ArrayList<MovieBriefDTO> performedMovies;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate.toString();
    }

    public String getNationality() {
        return nationality;
    }

    public ArrayList<MovieBriefDTO> getPerformedMovies() {
        return performedMovies;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setPerformedMovies(ArrayList<MovieBriefDTO> performedMovies) {
        this.performedMovies = performedMovies;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
