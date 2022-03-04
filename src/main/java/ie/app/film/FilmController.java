package ie.app.film;

import ie.app.actor.Actor;
import ie.app.actor.ActorManager;
import ie.exception.CustomException;
import ie.generic.controller.Controller;
import io.javalin.http.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FilmController extends Controller {
    private FilmView viewHandler;
    public FilmController() {
        this.viewHandler = new FilmView();
    }
    public void moviesHandler (Context ctx) throws CustomException, IOException {
        var films = FilmManager.getInstance().getElementsById(null);
        List<List<Actor>> filmCasts = new ArrayList<>();
        for(var film : films) {
            filmCasts.add(ActorManager.getInstance().getElementsById(film.getCast()));
        }
        ctx.html(viewHandler.getMoviesHtmlResponse(films, filmCasts));
    }

    public void movieHandler (Context ctx) throws CustomException, IOException {
        var movie_id = ctx.pathParam("movie_id");
        Film film = FilmManager.getInstance().getElementById(movie_id);
        List<Actor> cast = ActorManager.getInstance().getElementsById(film.getCast());

        ctx.html(viewHandler.getMovieHtml(film, cast));
    }
}
