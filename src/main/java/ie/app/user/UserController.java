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

    public void addWatchListHandler(Context ctx) throws CustomException, IOException {
        if (ctx.method() == "GET") {
            // TODO : check userId with valid email format (?is it necessary?)
            var userId = ctx.pathParam(UserRouter.UrlsPath.U_ID);
            // TODO : get "{movie_id}" route from FilmRouter
            var movieId = ctx.pathParamAsClass("{movie_id}", Integer.class).get();
            UserManager.getInstance().addToWatchList(userId, movieId.toString());
            ctx.html(viewHandler.getSuccessHtmlResponse());
        }
    }
    public void deleteWatchListHandler(Context ctx) throws IOException, CustomException {
        // TODO : check userId with valid email format (?is it necessary?)
        var userId = ctx.pathParam(UserRouter.UrlsPath.U_ID);
        // TODO : get "{movie_id}" route from FilmRouter
        var movieId = ctx.formParamAsClass("movie_id", Integer.class).get().toString();
        UserManager.getInstance().removeFromWatchList(userId, movieId);
        ctx.html(viewHandler.getSuccessHtmlResponse());
    }
}
