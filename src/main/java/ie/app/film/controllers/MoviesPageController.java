package ie.app.film.controllers;

import ie.Iemdb;
import ie.app.actor.Actor;
import ie.app.actor.ActorManager;
import ie.app.comment.CommentManager;
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
                var cast = ActorManager.getInstance().getElementsById(movie.getCast());
                var comments = CommentManager.getInstance().getElementsById(movie.getComments());
                request.setAttribute("movie", movie);
                request.setAttribute("cast", cast);
                request.setAttribute("comments", comments);
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
            try {
                var movieId = pathParts[0];
                switch (action) {
                    case Constant.ActionType.RATE:
                        FilmManager.getInstance().rateMovie(movieId, Iemdb.loggedInUser.getId(), Integer.parseInt(request.getParameter(Constant.FormInputNames.MOVIE_RATE)));
                        response.sendRedirect(Constant.URLS.MOVIES + "/" + movieId);
                        break;

                    case Constant.ActionType.LIKE:
                        var commentId = request.getParameter(Constant.FormInputNames.COMMENT_ID);
                        CommentManager.getInstance().voteComment(commentId, Iemdb.loggedInUser.getId(), +1);
                        response.sendRedirect(Constant.URLS.MOVIES + "/" + movieId);
                        break;

                    case Constant.ActionType.DISLIKE:
                        commentId = request.getParameter(Constant.FormInputNames.COMMENT_ID);
                        CommentManager.getInstance().voteComment(commentId, Iemdb.loggedInUser.getId(), -1);
                        response.sendRedirect(Constant.URLS.MOVIES + "/" + movieId);
                        break;

                    

                    default:
                        sendBadRequestResponse(request, response, Map.ofEntries(entry(Constant.FormInputNames.MOVIE_ACTION, "Action is not proper")));
                        break;
                }
            } catch (Exception e) {
                // TODO: handle exception if needed
                e.printStackTrace();
            }
        } else {
            switch (action) {
                case Constant.ActionType.SEARCH:
                    FilmManager.getInstance().setNameFilter(request.getParameter(Constant.FormInputNames.MOVIE_NAME));
                    response.sendRedirect(Constant.URLS.MOVIES);
                    break;
                case Constant.ActionType.CLEAR:
                    FilmManager.getInstance().setNameFilter(null);
                    response.sendRedirect(Constant.URLS.MOVIES);
                    break;
                case Constant.ActionType.SORT_DATE:
                    FilmManager.getInstance().setSortType(Constant.ActionType.SORT_DATE);
                    response.sendRedirect(Constant.URLS.MOVIES);
                    break;
                case Constant.ActionType.SORT_IMDB:
                    FilmManager.getInstance().setSortType(Constant.ActionType.SORT_IMDB);
                    response.sendRedirect(Constant.URLS.MOVIES);
                    break;
                default:
                    sendBadRequestResponse(request, response, Map.ofEntries(entry(Constant.FormInputNames.MOVIE_ACTION, "Action is not proper")));
                    break;
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
