package ie.iemdb.model;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.MovieAlreadyExistsException;
import ie.iemdb.util.types.Email;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class User {
    private Email email;
    private String password;
    private String nickname;
    private String name;
    private LocalDate birthDate;

    private ArrayList<Movie> watchList;

    private User(String email, String password, String nickname, String name, String birthDate) throws CustomException {
        this.watchList = new ArrayList<>();

        this.email = new Email(email);
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate);
    }

    public String getId() {
        return this.email.toString();
    }

    public void addToWatchList(Movie movie) throws CustomException {
        if (watchList.contains(movie))
            throw new MovieAlreadyExistsException();
        watchList.add(movie);
    }

    public void removeFromWatchList(String id) throws CustomException {
        for (var movie : this.watchList) {
            if (movie.getId().equals(id)) {
                watchList.remove(movie);
                return;
            }
        }
        throw new CustomException("MovieNotFoundInWatchList");
    }

    public boolean isOlderThan(Integer age) {
        return age <= Period.between(birthDate, LocalDate.now()).getYears();
    }

    public ArrayList<Movie> getWatchList() {
        return watchList;
    }

    public Boolean hasMovieInWatchList(Movie movie) {
        return watchList.contains(movie);
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getName() {
        return this.name;
    }
}
