package ie.app.film;

import ie.generic.router.Router;
import ie.util.types.Constant;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;

public class FilmRouter extends Router {
    private FilmController controller;
    public FilmRouter() {
        this.controller = new FilmController();
    }

    @Override
    public void addRoutes(Javalin javalin) {
        javalin.routes(() -> {
            path(Constant.Server.MOVIES, () -> {
                get(controller::moviesHandler);
                path(UrlPath.MOVIES_ID, () -> {

                });
            });
        });
    }

    public static class UrlPath {
        public static final String MOVIES = "/movies";
        public static final String MOVIES_ID = "{movie_id}";
    }
}
