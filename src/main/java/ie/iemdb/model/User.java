package ie.iemdb.model;

import ie.iemdb.exception.CustomException;
import ie.iemdb.exception.MovieAlreadyExistsException;
import ie.iemdb.exception.MovieNotFoundException;
import ie.iemdb.repository.Retriever;
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

    private ArrayList<Movie> watchList = null;
    private Retriever retriever;

    public User(String email, String password, String nickname, String name, String birthDate) throws CustomException {
        this.email = new Email(email);
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.birthDate = LocalDate.parse(birthDate);
    }

    public void setRetriever(Retriever retriever){
        this.retriever = retriever;
    }

    public String getId() {
        return this.email.toString();
    }

    public void addToWatchList(Movie movie) {
        getWatchList();
        if (!watchList.contains(movie))
            watchList.add(movie);
    }

    public void removeFromWatchList(String id) throws MovieNotFoundException {
        getWatchList();
        for (var movie : this.watchList) {
            if (movie.getId().equals(id)) {
                watchList.remove(movie);
                return;
            }
        }
        throw new MovieNotFoundException();
    }

    public boolean isOlderThan(Integer age) {
        return age <= Period.between(birthDate, LocalDate.now()).getYears();
    }

    public ArrayList<Movie> getWatchList() {
        if(this.watchList == null)
            this.watchList = this.retriever.getWatchlistForUser(this.email.toString());
        return watchList;
    }

    public Boolean hasMovieInWatchList(Movie movie) {
        return getWatchList().contains(movie);
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getName() {
        return this.name;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
