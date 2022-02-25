package ie.types;

import java.util.HashSet;
import java.util.Set;

public class Constant {
    public  static class Actor {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String B_DATE = "birthDate";
        public static final String NATION = "nationality";
    }
    public static class Movie {
        public static final String ID = "id";
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

    }
    public static class User {
        public static final String E_ID = "email";
        public static final String PASS = "password";
        public static final String NICKNAME = "nickname";
        public static final String NAME = "name";
        public static final String B_DATE = "birthDate";
    }
    public static class Comment {
        public static final String ID = "id";
        public static final String U_ID = "userEmail";
        public static final String M_ID = "movieId";
        public static final String CONTENT = "text";
        public static final String C_DATE = "createdDate";

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
        public static final String ADD_TO_WATCH_LIST = "addToWatchList";
        public static final String REMOVE_FROM_WATCH_LIST = "removeFromWatchList";
        public static final String GET_MOVIE_BY_ID = "getMovieById";
        public static final String GET_MOVIE_LIST = "getMovieList";
        public static final String VOTE_COMMENT = "voteComment";

    }
    public static enum Model {
        ACTOR,
        FILM,
        USER,
        COMMENT
    }
    public static enum SER_MODE {
        SHORT,
        LONG
    }
}