package ie.app.actor;

import ie.generic.router.Router;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class ActorRouter extends Router {
    private ActorController controller;

    public ActorRouter() { controller = new ActorController(); }
    @Override
    public void addRoutes(Javalin javalin) {
        javalin.routes(() -> {
            path(UrlsPath.ACTORS, () -> {
                path(UrlsPath.A_ID, () -> {
                    get(controller::actorHandler);
                });
            });
        });
    }
    public static class UrlsPath {
        public static final String ACTORS = "/actors";
        public static final String A_ID = "{actor_id}";
    }
}
