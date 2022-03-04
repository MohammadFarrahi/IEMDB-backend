package ie;

import ie.exception.*;
import ie.util.types.Constant;
import org.jsoup.Connection;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IemdbTest {
    Iemdb iemdb;
    Connection.Response response;

    @Before
    public void setup() throws CustomException {
        response = null;
        iemdb = new Iemdb();
        iemdb.fetchData();
        iemdb.startServer();
    }

    public void assert404Response() {
        assertEquals(404, response.statusCode());
    }
    public void assert403Response() {
        assertEquals(403, response.statusCode());
    }
    public void assert200Response() {
        assertEquals(200, response.statusCode());
    }
//    public void assertHtmlValue(String htmlElementId, String expectedText) {
//
//    }

}
//    // Testing rate movie
//
//    @Test
//    public void testSimpleRate() {
//        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 8}");
//        assertSuccessResponse(Constant.SuccessMessage.RATE_MOVIE);
//    }
//
//    @Test
//    public void testOutOfRangeScore() {
//        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"score\": 18}");
//        assertExceptionResponse(InvalidRateScoreException.message);
//    }
//
//    @Test
//    public void testRateMovieNotFound() {
//        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 15, \"score\": 18}");
//        assertExceptionResponse(MovieNotFoundException.message);
//    }
//
//    @Test
//    public void testRateUserNotFound() {
//        iemdb.runTextCommand("rateMovie", "{\"userEmail\": \"sajjaasdd@ut.ac.ir\", \"movieId\": 1, \"score\": 18}");
//        assertExceptionResponse(UserNotFoundException.message);
//    }
//
//    // Testing vote comment
//
//    @Test
//    public void testSimpleVote() {
//        iemdb.runTextCommand("addComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"text\": \"I love this movie.\"}");
//        iemdb.runTextCommand("voteComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");
//        assertSuccessResponse(Constant.SuccessMessage.VOTE_COMMENT);
//    }
//
//    @Test
//    public void testVoteUserNotFound() {
//        iemdb.runTextCommand("addComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"text\": \"I love this movie.\"}");
//        iemdb.runTextCommand("voteComment", "{\"userEmail\": \"sajjdssdad@ut.ac.ir\", \"commentId\": 1, \"vote\": 1}");
//
//        assertExceptionResponse(UserNotFoundException.message);
//    }
//
//    @Test
//    public void testVoteCommentNotFound() {
//        iemdb.runTextCommand("addComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 1, \"text\": \"I love this movie.\"}");
//        iemdb.runTextCommand("voteComment", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"commentId\": 21, \"vote\": 1}");
//
//        assertExceptionResponse(CommentNotFoundException.message);
//
//    }
//    //Testing get movie by genre
//
//
//    @Test
//    public void testSimpleGetMovie() {
//        iemdb.runTextCommand("getMoviesByGenre", "{\"genre\": \"Crime\"}");
//        assertResponse("{\"success\":true,\"data\":[{\"movieId\":1,\"name\":\"The Godfather\",\"director\":\"Francis Ford Coppola\",\"genres\":[\"Crime\",\"Drama\"],\"rating\":null}]}");
//    }
//
//    @Test
//    public void testEmptyGetMovie() {
//        iemdb.runTextCommand("getMoviesByGenre", "{\"genre\": \"Mystery\"}");
//        assertResponse("{\"success\":true,\"data\":[]}");
//    }
//
//
//    // Testing addToWatchList
//
//
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
//
//    @Test
//    public void testAgeLimit() {
//        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"saman@ut.ac.ir\", \"movieId\": 2}");
//        assertExceptionResponse(AgeLimitException.message);
//
//        iemdb.runTextCommand("getWatchList", "{\"userEmail\": \"saman@ut.ac.ir\"}");
//        assertResponse("{\"success\":true,\"data\":{\"WatchList\":[]}}");
//    }
