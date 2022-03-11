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

@WebServlet(Constant.URLS.MOVIES)
public class MoviesPageController extends Controller {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var pathParts = splitPathParams(request.getPathInfo());

        if(pathParts == null) {
            try {
                sendMoviesPage(request, response, FilmManager.getInstance().getElementsById(null));
            } catch (CustomException e) {
                send404Response(request, response, null);
            }
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        var action = request.getParameter(Constant.FormInputNames.MOVIE_ACTION);
        var pathParts = splitPathParams(request.getPathInfo());

        if(pathParts != null) {
            // TODO : special movie
        }
        switch (action) {
            case Constant.MovieActionType.SEARCH :
                break;
            case Constant.MovieActionType.CLEAR :
                break;
            case Constant.MovieActionType.SORT_DATE :
                break;
            case Constant.MovieActionType.SORT_IMDB :
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
