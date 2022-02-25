package ie;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IemdbTest {
    Iemdb iemdb;
    @Before
    public void setup() {
        iemdb = new Iemdb();
        iemdb.runTextCommand("addUser", "{\"email\": \"sara@ut.ac.ir\", \"password\": \"sara1234\", \"name\": \"Sara\", \"nickname\": \"sara\", \"birthDate\": \"1998-03-11\"}");
        iemdb.runTextCommand("addUser", "{\"email\": \"sajjad@ut.ac.ir\", \"password\": \"sajjad1234\", \"name\": \"Sajjad\", \"nickname\": \"sajjad\", \"birthDate\": \"2000-06-14\"}");

        iemdb.runTextCommand("addActor", "{\"id\": 1, \"name\": \"Marlon Brando\", \"birthDate\": \"1924-04-03\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 2, \"name\": \"Al Pacino\", \"birthDate\": \"1940-04-25\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 3, \"name\": \"James Caan\", \"birthDate\": \"1940-03-26\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 4, \"name\": \"Adrien Brody\", \"birthDate\": \"1973-04-14\", \"nationality\": \"American\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 5, \"name\": \"Thomas Kretschmann\", \"birthDate\": \"1962-09-08\", \"nationality\": \"German\"}");
        iemdb.runTextCommand("addActor", "{\"id\": 6, \"name\": \"Frank Finlay\", \"birthDate\": \"1926-08-06\", \"nationality\": \"British\"}");

        iemdb.runTextCommand("addMovie", "{\"id\": 1, \"name\": \"The Godfather\", \"summary\": \"The aging patriarch of an organized crime dynasty in postwar New York City transfers control of his clandestine empire to his reluctant youngest son.\", \"releaseDate\": \"1972-03-14\", \"director\": \"Francis Ford Coppola\", \"writers\": [\"Mario Puzo\", \"Francis Ford Coppola\"], \"genres\": [\"Crime\", \"Drama\"], \"cast\": [1, 2, 3], \"imdbRate\": 9.2, \"duration\": 175, \"ageLimit\": 14}");
        iemdb.runTextCommand("addMovie", "{\"id\": 2, \"name\": \"The Pianist\", \"summary\": \"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\", \"releaseDate\": \"2002-05-24\", \"director\": \"Roman Polanski\", \"writers\": [\"Ronald Harwood\", \"Wladyslaw Szpilman\"], \"genres\": [\"Biography\", \"Drama\", \"Music\"], \"cast\": [4, 5, 6], \"imdbRate\": 8.5, \"duration\": 150, \"ageLimit\": 12}");
    }



    // Testing addToWatchList
    @Test
    public void testSimpleAdd() {
        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 2}");
        assertEquals(iemdb.getResponse(), "{\"success\":true,\"data\":\"Movie added to watchlist successfully\"}");
        iemdb.runTextCommand("getWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\"}");
        assertEquals(iemdb.getResponse(), "{\"success\":true,\"data\":{\"WatchList\":[{\"id\":2,\"name\":\"The Pianist\",\"summary\":\"A Polish Jewish musician struggles to survive the destruction of the Warsaw ghetto of World War II.\",\"releaseDate\":\"2002-05-24\",\"director\":\"Roman Polanski\",\"writers\":[\"Ronald Harwood\",\"Wladyslaw Szpilman\"],\"genres\":[\"Biography\",\"Drama\",\"Music\"],\"duration\":150,\"ageLimit\":12,\"rating\":null}]}}");
    }

    @Test
    public void testMovieNotFound() {
        iemdb.runTextCommand("addToWatchList", "{\"userEmail\": \"sajjad@ut.ac.ir\", \"movieId\": 3}");
        assertEquals(iemdb.getResponse(), "{\"success\":false,\"data\":\"Movie not found\"}");
    }

    @Test
    public void testAgeLimit() {
        //TODO
    }

}
