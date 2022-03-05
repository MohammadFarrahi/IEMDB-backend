package ie;

import ie.exception.*;
import ie.util.types.Constant;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class IemdbTest {
    Iemdb iemdb = null;
    Connection.Response response = null;

    @Before
    public void setup() throws CustomException {
        iemdb = new Iemdb();
        iemdb.fetchData();
        iemdb.startServer();
    }

    @After
    public void tearDown() {
        iemdb.removeDatabase();
        iemdb.stopServer();
        iemdb = null;
        response = null;
    }

    public void assert404Response(int code) {
        assertEquals(404, code);
    }
    public void assert403Response(int code) {
        assertEquals(403, code);
    }
    public void assertHtmlValue(String htmlElementId, String expectedText) throws IOException {
        assertEquals(expectedText, response.parse().getElementById(htmlElementId).text());
    }

    @Test
    public void testRateMovieSuccess() throws IOException {
        var users = Iemdb.userIds;
        var films = Iemdb.filmIds;
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(0) + '/' + films.get(1) + "/8").execute();
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(1) + '/' + films.get(1) + "/7").execute();
        response = Jsoup.connect(Constant.Server.BASE + "/movies/" + films.get(1)).execute();
        assertHtmlValue("rating", "rating:7.5");

        // updating rate
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(1) + '/' + films.get(1) + "/9").execute();
        response = Jsoup.connect(Constant.Server.BASE + "/movies/" + films.get(1)).execute();
        assertHtmlValue("rating", "rating:8.5");
    }
    public void testRateMovieInvalidRate() throws IOException {
        var users = Iemdb.userIds;
        var films = Iemdb.filmIds;
        response = Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(0) + '/' + films.get(1) + "/12").execute();

    }
    public void testRateMovieInvalidId() throws IOException {
        var users = Iemdb.userIds;
        var films = Iemdb.filmIds;
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(1) + '/' + films.size() + 1 + "/7").execute();
    }
    @Test
    public void testRateMovieFail() {
        HttpStatusException e = assertThrows(HttpStatusException.class, this::testRateMovieInvalidRate);
        assert403Response(e.getStatusCode());
        e = assertThrows(HttpStatusException.class, this::testRateMovieInvalidId);
        assert404Response(e.getStatusCode());
    }
}
