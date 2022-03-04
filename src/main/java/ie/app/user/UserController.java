package ie.app.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import ie.app.film.FilmManager;
import ie.exception.CustomException;
import ie.generic.controller.Controller;
import io.javalin.http.Context;

import java.io.IOException;

public class UserController extends Controller {
    private UserView viewHandler;
    public UserController() { viewHandler = new UserView(); }
    public void getWatchListHandler(Context ctx) throws CustomException, IOException {
        // TODO : check userId with valid email format (?is it necessary?)
        var userId = ctx.pathParam(UserRouter.UrlsPath.U_ID);
        var user = UserManager.getInstance().getElementById(userId);
        var userWatchList = FilmManager.getInstance().getElementsById(user.getWatchList());
        ctx.html(viewHandler.getWatchListHtmlResponse(user, userWatchList));
    }
}
