package ie.app.film.controllers;

import ie.app.actor.Actor;
import ie.app.actor.ActorManager;
import ie.app.film.Film;
import ie.app.film.FilmManager;
import ie.exception.CustomException;
import ie.generic.controller.Controller;
import ie.util.types.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@WebServlet(Constant.URLS.MOVIES + "/*")
public class MoviesPageController extends Controller {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var pathParts = splitPathParams(request.getPathInfo());

        if (pathParts == null) {
            try {
                sendMoviesPage(request, response, FilmManager.getInstance().fetchFilmsForUser());
            } catch (CustomException e) {
                send404Response(request, response, null);
            }
        } else {
            //TODO: validate the url
            var movieId = pathParts[0];
            try {
                var movie = FilmManager.getInstance().getElementById(movieId);

                request.setAttribute("movie", movie);
                request.getRequestDispatcher(Constant.JSP.MOVIE).forward(request, response);


            } catch (CustomException e) {
                send404Response(request, response, null);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var action = request.getParameter(Constant.FormInputNames.MOVIE_ACTION);
        var pathParts = splitPathParams(request.getPathInfo());

        if (pathParts != null) {
            // TODO : special movie
        }
        switch (action) {
            case Constant.MovieActionType.SEARCH:
                FilmManager.getInstance().setNameFilter(request.getParameter(Constant.FormInputNames.MOVIE_NAME));
                response.sendRedirect(Constant.URLS.MOVIES);
                break;
            case Constant.MovieActionType.CLEAR:
                FilmManager.getInstance().setNameFilter(null);
                response.sendRedirect(Constant.URLS.MOVIES);
                break;
            case Constant.MovieActionType.SORT_DATE:
                FilmManager.getInstance().setSortType(Constant.MovieActionType.SORT_DATE);
                response.sendRedirect(Constant.URLS.MOVIES);
                break;
            case Constant.MovieActionType.SORT_IMDB:
                FilmManager.getInstance().setSortType(Constant.MovieActionType.SORT_IMDB);
                response.sendRedirect(Constant.URLS.MOVIES);
                break;
            default:
                sendBadRequestResponse(request, response, Map.ofEntries(entry(Constant.FormInputNames.MOVIE_ACTION, "Action is not proper")));
                break;
        }
    }

    private void sendMoviesPage(HttpServletRequest request, HttpServletResponse response, List<Film> films)
            throws ServletException, IOException, CustomException {
        List<List<Actor>> filmCasts = new ArrayList<>();
        for (var film : films) {
            filmCasts.add(ActorManager.getInstance().getElementsById(film.getCast()));
        }
        request.setAttribute("movies", films);
        request.setAttribute("moviesCast", filmCasts);
        request.getRequestDispatcher(Constant.JSP.MOVIES).forward(request, response);
    }
}
