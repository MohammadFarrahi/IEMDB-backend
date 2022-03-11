package ie.app.actor.controllers;

import ie.app.actor.ActorManager;
import ie.app.film.FilmManager;
import ie.exception.CustomException;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(Constant.URLS.ACTOR)
public class ActorPageController extends Controller {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var pathParts = splitPathParams(request.getPathInfo());

        // validation logic
        var errorMessages = validatePath(pathParts, Constant.PathType.ID);
        if(errorMessages == null)
            send404Response(request, response, null);
        if(!errorMessages.isEmpty())
            sendBadRequestResponse(request, response, errorMessages);
        // business logic
        try {
            var actor = ActorManager.getInstance().getElementById(pathParts[0]);
            var performedMovies = FilmManager.getInstance().getElementsById(actor.getPerformedMovies());
            request.setAttribute("actor", actor);
            request.setAttribute("movies", performedMovies);
            request.getRequestDispatcher(Constant.JSP.ACTOR).forward(request, response);
        } catch (CustomException e) {
            errorMessages.put("InvalidActor", "Actor Not Found");
            send404Response(request, response, errorMessages);
        }
    }
}
