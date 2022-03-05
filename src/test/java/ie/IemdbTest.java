package ie;

import ie.app.film.FilmManager;
import ie.app.user.UserManager;
import ie.exception.*;
import ie.generic.model.JsonHandler;
import ie.util.types.Constant;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class IemdbTest {
    Iemdb iemdb = null;
    Document responseBody = null;

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
        responseBody = null;
    }

    public void assert404Response(int code) {
        assertEquals(404, code);
    }
    public void assert403Response(int code) {
        assertEquals(403, code);
    }
    public void assertHtmlValue(String htmlElementId, String expectedText) throws IOException {
        assertEquals(expectedText, responseBody.getElementById(htmlElementId).text());
    }
    public void assertHtmlValue(String htmlElementName, int elementIndex, String expectedText) throws IOException {
        assertEquals(expectedText, responseBody.select(htmlElementName).get(elementIndex).text());
    }

    @Test
    public void testRateMovieSuccess() throws IOException {
        var users = Iemdb.userIds;
        var films = Iemdb.filmIds;
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(0) + '/' + films.get(1) + "/8").execute();
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(1) + '/' + films.get(1) + "/7").execute();
        responseBody = Jsoup.connect(Constant.Server.BASE + "/movies/" + films.get(1)).execute().parse();
        assertHtmlValue("rating", "rating:7.5");

        // updating rate
        Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(1) + '/' + films.get(1) + "/9").execute();
        responseBody = Jsoup.connect(Constant.Server.BASE + "/movies/" + films.get(1)).execute().parse();
        assertHtmlValue("rating", "rating:8.5");
    }
    public void testRateMovieInvalidRate() throws IOException {
        var users = Iemdb.userIds;
        var films = Iemdb.filmIds;
        responseBody = Jsoup.connect(Constant.Server.BASE + "/rateMovie/" + users.get(0) + '/' + films.get(1) + "/12").execute().parse();

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
    @Test
    public void testWatchListSuccess() throws IOException, CustomException {
        var userJson = JsonHandler.getNodeOfObject(UserManager.getInstance().getElementById(iemdb.userIds.get(0)));
        var filmJson = JsonHandler.getNodeOfObject(FilmManager.getInstance().getElementById(iemdb.filmIds.get(0)));

        var userId = userJson.get(Constant.User.E_ID).asText();
        var userNickname = userJson.get(Constant.User.NICKNAME).asText();
        var filmId = filmJson.get(Constant.Movie.ID_G).asText();
        var filmName = filmJson.get(Constant.Movie.NAME).asText();

        Jsoup.connect(Constant.Server.BASE + "/watchList/" + userId + '/' + filmId).execute();
        responseBody = Jsoup.connect(Constant.Server.BASE + "/watchList/" + userId).execute().parse();
        assertHtmlValue("nickname", "nickname: @" + userNickname);
        assertHtmlValue("td", 0, filmName);
    }
}
