package ie.network;

import ie.app.film.FilmController;
import ie.generic.router.Router;
import ie.util.types.Constant;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Server {
    private final Javalin javalinServer;
    public Server(Router[] routers) {
        javalinServer = Javalin.create();
        for(var router : routers) {
            router.addRoutes(javalinServer);
        }
    }
    public void runServer() {
        javalinServer.start(Constant.Server.HOST, Constant.Server.PORT);
    }
}
