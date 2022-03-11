package ie.app.user.controllers;

import ie.Iemdb;
import ie.app.film.Film;
import ie.app.film.FilmManager;
import ie.app.user.UserManager;
import ie.exception.ObjectNotFoundException;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
}
