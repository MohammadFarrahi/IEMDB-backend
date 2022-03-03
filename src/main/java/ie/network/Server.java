package ie.network;

import ie.app.film.FilmController;
import ie.util.types.Constant;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Server {
    private final Javalin javalinServer;
    public Server(FilmController filmController) {
        javalinServer = Javalin.create();
        javalinServer.routes(() -> {
            path(Constant.Server.MOVIES, () -> {
                get(filmController::moviesHandler);
            });
        });
    }
    public void runServer() {
        javalinServer.start(Constant.Server.HOST, Constant.Server.PORT);
    }
}
