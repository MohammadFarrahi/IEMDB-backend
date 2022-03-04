package ie.app.actor;

import ie.app.film.FilmManager;
import ie.exception.CustomException;
import ie.generic.controller.Controller;
import io.javalin.http.Context;

import java.io.IOException;

public class ActorController extends Controller {
    private ActorView viewHandler;
    public ActorController() {
        this.viewHandler = new ActorView();
    }

    public void actorHandler(Context ctx) throws CustomException, IOException {
        var actorId = ctx.pathParamAsClass(ActorRouter.UrlsPath.A_ID, Integer.class).get();
        var actor = ActorManager.getInstance().getElementById(actorId.toString());
        var performedMovies = FilmManager.getInstance().getElementsById(actor.getPerformedMovies());
        ctx.html(viewHandler.getActorHtmlResponse(actor, performedMovies));
    }
}
