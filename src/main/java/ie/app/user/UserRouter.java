package ie.app.user;

import ie.generic.router.Router;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UserRouter extends Router {
    private UserController controller;
    public UserRouter() { this.controller = new UserController(); }
    @Override
    public void addRoutes(Javalin javalin) {
        javalin.routes(() -> {
            path(UrlsPath.W_LIST, () -> {
                post(controller::addToWatchlistHandler);
                path(UrlsPath.U_ID, () -> {
                    get(controller::getWatchListHandler);
                    post(controller::deleteWatchListHandler);
                    // TODO : get "{movie_id}" route from FilmRouter
                    path("{movie_id}", ()-> {
                        get(controller::addWatchListHandler);
                    });
                });
            });
        });
    }
    public static class UrlsPath {
        public static final String W_LIST = "/watchList";
        public static final String U_ID = "{user_id}";
    }
}
