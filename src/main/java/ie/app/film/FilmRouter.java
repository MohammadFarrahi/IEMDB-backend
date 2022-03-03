package ie.app.film;

import ie.generic.router.Router;
import ie.util.types.Constant;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.get;

public class FilmRouter extends Router {
    FilmController controller;
    public FilmRouter() {
        this.controller = new FilmController();
    }

    @Override
    public void addRoutes(Javalin javalin) {
        javalin.routes(() -> {
            path(Constant.Server.MOVIES, () -> {
                get(controller::moviesHandler);
            });
        });
    }
}
