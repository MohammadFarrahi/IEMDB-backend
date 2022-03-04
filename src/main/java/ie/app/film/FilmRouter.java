package ie.app.film;

import ie.generic.router.Router;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class FilmRouter extends Router {
    private FilmController controller;

    public FilmRouter() {
        this.controller = new FilmController();
    }

    @Override
    public void addRoutes(Javalin javalin) {
        javalin.routes(() -> {
            path(UrlPath.MOVIES, () -> {
                get(controller::moviesHandler);
                path(UrlPath.SEARCH, () -> {
                    path("{start_year}/{end_year}", () -> {
                        get(controller::filterMoviesHandler);
                    });
                    path ("{genre}", () -> {
                        get(controller::filterMoviesHandler);
                    });
                });
                path(UrlPath.MOVIES_ID, () -> {
                    get(controller::movieHandler);
                    path("/add-to-wlist", () -> {
                        post(controller::addToWatchlistHandler);
                    });
                    path("/rateMovie", () -> {
                        post(controller::rateMovieFormHandler);
                    });
                    path("/vote-comment", () -> {
                        post(controller::voteCommentHandler);
                    });
                });
            });
            //TODO: handler this mess of urls. is there any better way?
            path("rateMovie/{user_id}/{movie_id}/{rate}", () -> {
                get(controller::rateMovieUrlHandler);
            });
        });
    }

    public static class UrlPath {
        public static final String MOVIES = "/movies";
        public static final String MOVIES_ID = "{movie_id}";
        public static final String ADD_TO_W_LIST = "add-to-wlist";
        public static final String RATE_MOVIE = "rateMovie";
        public static final String VOTE_COMMENT = "vote-comment";
        public static final String SEARCH = "search";
    }
}
