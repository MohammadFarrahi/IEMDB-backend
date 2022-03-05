package ie.network;

import ie.exception.ObjectNotFoundException;
import ie.generic.controller.Controller;
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
        javalinServer.exception(ValidationException.class, Controller::Exception403Handler);
        javalinServer.exception(NotFoundResponse.class, Controller::Exception404Handler);
    }
    public void runServer() {
        javalinServer.start(Constant.Server.HOST, Constant.Server.PORT);
    }
    public void stopServer() { javalinServer.stop(); }
    // TODO : Maybe this is not the best place. (even ServerExceptionController)
    private void commonExceptionHandler(Exception e, Context ctx) {
        if(e instanceof ObjectNotFoundException) {
            Controller.Exception404Handler(e, ctx);
        } else {
            Controller.Exception403Handler(e, ctx);
        }
    }
}
