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
//                post(controller::postWatchListItemHandler);
                path(UrlsPath.U_ID, () -> {
                    get(controller::getWatchListHandler);
                });
            });
        });
    }
    public static class UrlsPath {
        public static final String W_LIST = "/watchList";
        public static final String U_ID = "{user_id}";
    }
}
