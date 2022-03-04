package ie.network;

import ie.exception.ObjectNotFoundException;
import ie.generic.router.Router;
import ie.util.types.Constant;
import io.javalin.Javalin;
import io.javalin.core.validation.ValidationException;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Server {
    private final Javalin javalinServer;
    public Server(Router[] routers) {
        javalinServer = Javalin.create();
        for(var router : routers) {
            router.addRoutes(javalinServer);
        }
        javalinServer.exception(Exception.class, this::commonExceptionHandler);
        javalinServer.exception(ValidationException.class, ServerExceptionController::Exception403Handler);
        javalinServer.exception(NotFoundResponse.class, ServerExceptionController::Exception404Handler);
    }
    public void runServer() {
        javalinServer.start(Constant.Server.HOST, Constant.Server.PORT);
    }
    // TODO : Maybe this is not the best place. (even ServerExceptionController)
    private void commonExceptionHandler(Exception e, Context ctx) {
        System.out.println(e.getMessage()); // TODO : remove this later
        e.printStackTrace();
        if(e instanceof ObjectNotFoundException) {
            ServerExceptionController.Exception404Handler(e, ctx);
        } else {
            ServerExceptionController.Exception403Handler(e, ctx);
        }
    }
}
