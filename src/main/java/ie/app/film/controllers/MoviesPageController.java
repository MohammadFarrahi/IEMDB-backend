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
