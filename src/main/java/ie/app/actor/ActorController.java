package ie.app.actor;

import ie.exception.CustomException;
import ie.generic.controller.Controller;
import io.javalin.http.Context;

public class ActorController extends Controller {
    private ActorView viewHandler;
    public ActorController() {
        this.viewHandler = new ActorView();
    }

    public void actorHandler(Context ctx) throws CustomException {
        var actorId = ctx.pathParamAsClass(ActorRouter.UrlsPath.A_ID, Integer.class).get();
        var actor = ActorManager.getInstance().getElementById(actorId.toString());
        ctx.html(viewHandler.getActorHtmlResponse(actor));
    }
}
