package ie.app.user.controllers;

import ie.Iemdb;
import ie.app.film.Film;
import ie.app.film.FilmManager;
import ie.app.user.UserManager;
import ie.exception.CustomException;
import ie.exception.ObjectNotFoundException;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

import static java.util.Map.entry;

@WebServlet(Constant.URLS.WATCH_LIST)
public class UserWatchlist extends Controller {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {

            var movieIds = Iemdb.loggedInUser.getWatchList();
            var movies = FilmManager.getInstance().getElementsById(movieIds);

            var recMovieIds = UserManager.getInstance().getRecommendedWatchlist(Iemdb.loggedInUser);
            var recMovies = FilmManager.getInstance().getElementsById(recMovieIds);

            request.setAttribute("movies", movies);
            request.setAttribute("recMovies", recMovies);
            request.getRequestDispatcher(Constant.JSP.W_LIST).forward(request, response);

        } catch (ObjectNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var movieId = request.getParameter(Constant.FormInputNames.MOVIE_ID);

        // validation logic
        var errorMessages = validateForm(Map.ofEntries(entry(Constant.FormInputNames.MOVIE_ID, movieId)));
        if(!errorMessages.isEmpty())
            sendBadRequestResponse(request, response, errorMessages);

        try {
            UserManager.getInstance().removeFromWatchList(Iemdb.loggedInUser.getId(), movieId);
            response.sendRedirect(Constant.URLS.WATCH_LIST);
        } catch (CustomException e) {
            send404Response(request, response, errorMessages);
        }
    }
}
