package ie;

import ie.exception.*;
import ie.util.types.Constant;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IemdbTest {
    Iemdb iemdb;

    public void assertResponse(String message) {
        assertEquals(message, iemdb.getResponse());
    }

    public void assertExceptionResponse(String exceptionMessage) {
        String response = "{\"success\":false,\"data\":\"" + exceptionMessage + "\"}";
        assertResponse(response);
    }

    public void assertSuccessResponse(String successMessage) {
        String response = "{\"success\":true,\"data\":\"" + successMessage + "\"}";
        assertResponse(response);
    }

    @Before
    public void setup() {
        iemdb = new Iemdb();
        iemdb.runTextCommand("addUser", "{\"email\": \"sara@ut.ac.ir\", \"password\": \"sara1234\", \"name\": \"Sara\", \"nickname\": \"sara\", \"birthDate\": \"1998-03-11\"}");
        iemdb.runTextCommand("addUser", "{\"email\": \"sajjad@ut.ac.ir\", \"password\": \"sajjad1234\", \"name\": \"Sajjad\", \"nickname\": \"sajjad\", \"birthDate\": \"2000-06-14\"}");
        iemdb.runTextCommand("addUser", "{\"email\": \"saman@ut.ac.ir\", \"password\": \"saman1234\", \"name\": \"Saman\", \"nickname\": \"saman\", \"birthDate\": \"2014-01-01\"}");

        iemdb.runTextCommand("addActor", "{\"id\": 1, \"name\": \"Marlon Brando\", \"birthDate\": \"1924-04-03\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 2, \"name\": \"Al Pacino\", \"birthDate\": \"1940-04-25\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 3, \"name\": \"James Caan\", \"birthDate\": \"1940-03-26\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 4, \"name\": \"Adrien Brody\", \"birthDate\": \"1973-04-14\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 5, \"name\": \"Thomas Kretschmann\", \"birthDate\": \"1962-09-08\", \"nationality\": \"German\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 6, \"name\": \"Frank Finlay\", \"birthDate\": \"1926-08-06\", \"nationality\": \"British\"}");

        iemdb.runTextCommand("addMovie", "{\"id\": 1, \"name\": \"The Godfather\", \"summary\": \"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\", \"releaseDate\": \"1972-03-14\", \"director\": \"Francis Ford Coppola\", \"writers\": [\"Mario Puzo\", \"Francis Ford Coppola\"], \"genres\": [\"Crime\", \"Drama\"], \"cast\": [1, 2, 3], \"imdbRate\": 9.2, \"duration\": 175, \"ageLimit\": 14}");
        iemdb.runTextCommand("addMovie", "{\"id\": 2, \"name\": \"The Pianist\", \"summary\": \"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\", \"releaseDate\": \"2002-05-24\", \"director\": \"Roman Polanski\", \"writers\": [\"Ronald Harwood\", \"Wladyslaw Szpilman\"], \"genres\": [\"Biography\", \"Drama\", \"Music\"], \"cast\": [4, 5, 6], \"imdbRate\": 8.5, \"duration\": 150, \"ageLimit\": 12}");
    }

    // Testing rate movie

    @Test
    public void testSimpleRate() {
        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 8}");
        assertSuccessResponse(Constant.SuccessMessage.RATE_MOVIE);
    }

    @Test
    public void testOutOfRangeScore() {
        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 18}");
        assertExceptionResponse(InvalidRateScoreException.message);
    }

//    @Test
//    public void testRateMovieNotFound() {
//        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 15, \"score\": 18}");
//        assertExceptionResponse(MovieNotFoundException.message);
//    }

    @Test
    public void testRateUserNotFound() {
        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjaasdd@ut.ac.ir\", \"movieId\": 1, \"score\": 18}");
        assertExceptionResponse(UserNotFoundException.message);
    }

    // Testing vote comment

    @Test
    public void testSimpleVote() {
        iemdb.runTextCommand("addComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"text\": \"I love this movie.\"}");
        iemdb.runTextCommand("voteComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");
        assertSuccessResponse(Constant.SuccessMessage.VOTE_COMMENT);
    }

    @Test
    public void testVoteUserNotFound() {
        iemdb.runTextCommand("addComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"text\": \"I love this movie.\"}");
        iemdb.runTextCommand("voteComment", "{\"userEmail\": \"sajjdssdad@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");

        assertExceptionResponse(UserNotFoundException.message);
    }

    @Test
    public void testVoteCommentNotFound() {
        iemdb.runTextCommand("addComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"text\": \"I love this movie.\"}");
        iemdb.runTextCommand("voteComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"commentId\": 21, \"vote\": 1}");

        assertExceptionResponse(CommentNotFoundException.message);

    }
    //Testing get movie by genre

    // TODO : make json fields in order, rightnow movieId is last field.

//    @Test
//    public void testSimpleGetMovie() {
//        iemdb.runTextCommand("getMoviesByGenre", "{\"genre\": \"Crime\"}");
//        assertResponse("{\"success\":true,\"data\":[{\"movieId\":1,\"name\":\"The Godfather\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"rating\":null}]}");
//    }

    @Test
    public void testEmptyGetMovie() {
        iemdb.runTextCommand("getMoviesByGenre", "{\"genre\": \"Mystery\"}");
        assertResponse("{\"success\":true,\"data\":[]}");
    }


    // Testing addToWatchList

    // TODO : make json fields in order, rightnow movieId is last field.

//    @Test
//    public void testSimpleAdd() {
//        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 2}");
//        assertSuccessResponse(Constant.SuccessMessage.ADD_TO_WATCH_LIST);
//
//        iemdb.runTextCommand("getWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\"}");
//        assertResponse("{\"success\":true,\"data\":{\"WatchList\":[{\"movieId\":2,\"name\":\"The Pianist\",\"director\":\"Roman Polanski\",\"genres\":[\"Biography\",\"Drama\",\"Music\"],\"rating\":null}]}}");
//    }
//
//    @Test
//    public void testMovieNotFound() {
//        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 3}");
//        assertExceptionResponse(MovieNotFoundException.message);
//    }

    @Test
    public void testAgeLimit() {
        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"saman@ut.ac.ir\", \"movieId\": 2}");
        assertExceptionResponse(AgeLimitException.message);

        iemdb.runTextCommand("getWatchList", "{\"userEmail\": \"saman@ut.ac.ir\"}");
        assertResponse("{\"success\":true,\"data\":{\"WatchList\":[]}}");
    }

}
