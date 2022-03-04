package ie.util.types;

import java.util.HashSet;
import java.util.Set;

public class Constant {
    public  static class Actor {
        public static final String ID_S = "id";
        public static final String ID_G = "actorId";
        public static final String NAME = "name";
        public static final String B_DATE = "birthDate";
        public static final String NATION = "nationality";
        public static final String MOVIES = "performedMovies";
        public static final Set<String> REMOVABLE_SHORT_SER = new HashSet<String>(){{
            add(B_DATE);
            add(NATION);
            add(MOVIES);
        }};
    }
    public static class Movie {
        public static final String ID_S = "id";
        public static final String ID_G = "movieId";
        public static final String NAME = "name";
        public static final String SUMM = "summary";
        public static final String R_DATE = "releaseDate";
        public static final String DIRECTOR = "director";
        public static final String WRITERS = "writers";
        public static final String GENRE = "genres";
        public static final String CAST = "cast";
        public static final String DURATION = "duration";
        public static final String AGE_L = "ageLimit";
        public static final String IMDB = "imdbRate";
        public static final String RATING = "rating";
        public static final String COMMENTS = "comments";

        public static final Set<String> REMOVABLE_SHORT_SER = new HashSet<String>(){{
            add(SUMM);
            add(R_DATE);
            add(WRITERS);
            add(CAST);
            add(DURATION);
            add(AGE_L);
            add(IMDB);
            add(COMMENTS);
        }};

    }
    public static class User {
        public static final String E_ID = "email";
        public static final String PASS = "password";
        public static final String NICKNAME = "nickname";
        public static final String NAME = "name";
        public static final String B_DATE = "birthDate";
    }
    public static class Comment {
        public static final String ID = "commentId";
        public static final String U_ID = "userEmail";
        public static final String M_ID = "movieId";
        public static final String CONTENT = "text";
        public static final String C_DATE = "createdDate";
        public static final String LIKES = "like";
        public static final String DISLIKES = "dislike";

        public static final Set<String> REMOVABLE_SHORT_SER = new HashSet<String>(){{
            add(M_ID);
            add(C_DATE);
        }};

        public static Set<String> getSet(){
            return new HashSet<String>(){{
                add(U_ID);
                add(M_ID);
                add(CONTENT);
            }};
        }
    }

    public static class WatchList {
        public static final String U_ID = "userEmail";
        public static final String M_ID = "movieId";

        public static Set<String> getSet(){
            return new HashSet<String>(){{
                add(U_ID);
                add(M_ID);
            }};
        }
    }

    public static class Rate {
        public static final String U_ID = "userEmail";
        public static final String M_ID = "movieId";
        public static final String RATE = "score";

        public static Set<String> getSet(){
            return new HashSet<String>(){{
                add(U_ID);
                add(M_ID);
                add(RATE);
            }};
        }
    }

    public static class Response {
        public static final String STATUS = "success";
        public static final String DATA = "data";
    }
    public static class Vote {
        public static final String U_ID = "userEmail";
        public static final String C_ID = "commentId";
        public static final String VOTE = "vote";

        public static Set<String> getSet(){
            return new HashSet<String>(){{
                add(U_ID);
                add(C_ID);
                add(VOTE);
            }};
        }
    }
    public static class Command {
        public static final String ADD_USER = "addUser";
        public static final String ADD_MOVIE = "addMovie";
        public static final String ADD_ACTOR = "addActor";
        public static final String ADD_COMMENT = "addComment";
        public static final String RATE_MOVIE = "rateMovie";
        public static final String VOTE_COMMENT = "voteComment";
        public static final String ADD_TO_WATCH_LIST = "addToWatchList";
        public static final String REMOVE_FROM_WATCH_LIST = "removeFromWatchList";
        public static final String GET_MOVIE_BY_ID = "getMovieById";
        public static final String GET_MOVIE_LIST = "getMoviesList";
        public static final String GET_MOVIES_BY_GENRE = "getMoviesByGenre";
        public static final String GET_WATCH_LIST = "getWatchList";
    }
    public static class SuccessMessage {
        public static final String ADD_USER = "user added successfully";
        public static final String ADD_ACTOR = "actor added successfully";
        public static final String ADD_MOVIE = "movie added successfully";
//        public static final String ADD_COMMENT = "comment added successfully";
        public static final String RATE_MOVIE = "movie rated successfully";
        public static final String VOTE_COMMENT = "comment voted successfully";
        public static final String ADD_TO_WATCH_LIST = "movie added to watchlist successfully";
        public static final String REMOVE_FROM_WATCH_LIST = "movie removed from watchlist successfully";
    }
    public static enum SER_MODE {
        SHORT,
        LONG
    }
    public static class FetchApiUrl {
        public static final String BASE = "http://138.197.181.131:5000/api";
        public static final String ACTOR = "/actors";
        public static final String USER = "/users";
        public static final String MOVIE = "/movies";
        public static final String COMMENT = "/comments";
    }
    public static class Server {
        public static final String HOST = "localhost";
        public static final int PORT = 8080;
    }
    public static class Template {
        public static final String MOVIES = "src/main/resources/movies.html";
        public static final String MOVIE = "src/main/resources/movie.html";
        public static final String VOTE_C_FORM = "src/main/resources/voteCommentForm.html";

        public static final String ACTOR = "src/main/resources/actor.html";

        public static final String W_LIST = "src/main/resources/watchlist.html";
        public static final String W_LIST_R_F = "src/main/resources/removeWListForm.html";

        public static final String _404_ = "src/main/resources/404.html";
        public static final String _403_ = "src/main/resources/403.html";
        public static final String SUCCESS_200 = "src/main/resources/200.html";
    }
}