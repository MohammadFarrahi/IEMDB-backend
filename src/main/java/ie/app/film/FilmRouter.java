package ie.app.film;

import ie.generic.router.Router;
import ie.util.types.Constant;
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
                path(UrlPath.MOVIES_ID, () -> {
                    get(controller::movieHandler);
                    path("/add-to-wlist", () -> {
                        post(controller::addToWatchlist);
                    });
                    path("/rate-movie", () -> {
                        post(controller::rateMovie);
                    });
                    path("/vote-comment", () -> {
                        post(controller::voteComment);
                    });
                });
            });
        });
    }

    public static class UrlPath {
        public static final String MOVIES = "/movies";
        public static final String MOVIES_ID = "{movie_id}";
        public static final String ADD_TO_W_LIST = "add-to-wlist";
        public static final String RATE_MOVIE = "rate-movie";
        public static final String VOTE_COMMENT = "vote-comment";
    }
}
