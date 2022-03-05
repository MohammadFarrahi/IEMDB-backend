package ie.app.film;

import ie.app.actor.Actor;
import ie.app.actor.ActorManager;
import ie.app.comment.Comment;
import ie.app.comment.CommentManager;
import ie.app.user.UserManager;
import ie.exception.CustomException;
import ie.generic.controller.Controller;
import io.javalin.http.Context;
import org.eclipse.jetty.util.IO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilmController extends Controller {
    private FilmView viewHandler;

    public FilmController() {
        this.viewHandler = new FilmView();
    }

    public void moviesHandler(Context ctx) throws CustomException, IOException {
        var films = FilmManager.getInstance().getElementsById(null);
        List<List<Actor>> filmCasts = new ArrayList<>();
        for (var film : films) {
            filmCasts.add(ActorManager.getInstance().getElementsById(film.getCast()));
        }
        ctx.html(viewHandler.getMoviesHtmlResponse(films, filmCasts));
    }

    public void movieHandler(Context ctx) throws CustomException, IOException {
        var movieId = ctx.pathParamAsClass("movie_id", Integer.class).get().toString();

        Film film = FilmManager.getInstance().getElementById(movieId);
        List<Actor> cast = ActorManager.getInstance().getElementsById(film.getCast());
        List<Comment> comments = CommentManager.getInstance().getElementsById(film.getComments());
        ctx.html(viewHandler.getMovieHtml(film, cast, comments));
    }

    public void filterMoviesHandler(Context ctx) throws CustomException, IOException {
        ArrayList<String> filteredFilmIds;
        if (ctx.pathParamMap().size() == 2) {
            var startYear = ctx.pathParamAsClass("start_year", Integer.class).get();
            var endYear = ctx.pathParamAsClass("end_year", Integer.class).get();
            filteredFilmIds = FilmManager.getInstance().filterElementsByYear(startYear, endYear);
        } else {
            var genre = ctx.pathParam("genre");
            filteredFilmIds = FilmManager.getInstance().filterElementsByGenre(genre);
        }

        var films = FilmManager.getInstance().getElementsById(filteredFilmIds);
        List<List<Actor>> filmCasts = new ArrayList<>();
        for (var film : films) {
            filmCasts.add(ActorManager.getInstance().getElementsById(film.getCast()));
        }
        ctx.html(viewHandler.getMoviesHtmlResponse(films, filmCasts));
    }

    public void rateMovieFormHandler(Context ctx) throws CustomException, IOException {

        var movieId = ctx.pathParamAsClass("movie_id", Integer.class).get().toString();

        var userId = ctx.formParam("user_id");
        var rate = ctx.formParamAsClass("quantity", Integer.class).get();

        FilmManager.getInstance().rateMovie(movieId, userId, rate);
        ctx.html(viewHandler.getSuccessHtmlResponse());
    }

    public void rateMovieUrlHandler(Context ctx) throws CustomException, IOException {
        var movieId = ctx.pathParamAsClass("movie_id", Integer.class).get().toString();
        var userId = ctx.pathParam("user_id");
        var rate = ctx.pathParamAsClass("rate", Integer.class).get();
        FilmManager.getInstance().rateMovie(movieId, userId, rate);
        ctx.html(viewHandler.getSuccessHtmlResponse());
    }
}
