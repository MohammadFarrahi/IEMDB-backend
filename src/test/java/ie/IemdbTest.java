package ie;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IemdbTest {
    Iemdb iemdb;

    public void assertResponse(String message){
        assertEquals(message, iemdb.getResponse());
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
        System.out.println(iemdb.getResponse());
        assertResponse("{\"success\":true,\"data\":\"movie rated successfully\"}");

    }

    // Testing vote comment

    //Testing get movie by genre

    // Testing addToWatchList
    @Test
    public void testSimpleAdd() {
        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 2}");
        assertResponse("{\"success\":true,\"data\":\"Movie added to watchlist successfully\"}");

        iemdb.runTextCommand("getWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\"}");
        assertResponse("{\"success\":true,\"data\":{\"WatchList\":[{\"movieId\":2,\"name\":\"The Pianist\",\"director\":\"Roman Polanski\",\"genres\":[\"Biography\",\"Drama\",\"Music\"],\"rating\":null}]}}");
    }

    @Test
    public void testMovieNotFound() {
        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 3}");
        assertResponse("{\"success\":false,\"data\":\"Movie not found\"}");
    }

    @Test
    public void testAgeLimit() {
        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"saman@ut.ac.ir\", \"movieId\": 2}");
        assertResponse("{\"success\":false,\"data\":\"you age is not good\"}");

        iemdb.runTextCommand("getWatchList", "{\"userEmail\": \"saman@ut.ac.ir\"}");
        assertResponse("{\"success\":true,\"data\":{\"WatchList\":[]}}");
    }

}
