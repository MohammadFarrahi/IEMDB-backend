package ie.app.film;

import com.fasterxml.jackson.databind.JsonNode;
import ie.app.actor.Actor;
import ie.app.comment.Comment;
import ie.app.comment.CommentRouter;
import ie.app.user.UserRouter;
import ie.generic.model.JsonHandler;
import ie.generic.view.View;
import ie.util.types.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilmView extends View {
    public String getMoviesHtmlResponse(List<Film> films, List<List<Actor>> filmsCast) throws IOException {
        var template = Jsoup.parse(new File(Constant.Template.MOVIES), "UTF-8");
        var table = template.select("table").first();
        var index = 0;
        for (var film : films) {
            var filmJson = JsonHandler.getNodeOfObject(film);
            var filmHtml = new Element("tr");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.NAME).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.SUMM).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.R_DATE).asText() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.DIRECTOR).asText() + "</td>");
            filmHtml.append("<td>" + View.getCSVFromList(filmJson.get(Constant.Movie.WRITERS).toPrettyString()) + "</td>");
            filmHtml.append("<td>" + View.getCSVFromList(filmJson.get(Constant.Movie.GENRE).toPrettyString()) + "</td>");
            filmHtml.append("<td>" + getActorNames(filmsCast.get(index++)) + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.IMDB).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.RATING).asDouble() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.DURATION).asInt() + "</td>");
            filmHtml.append("<td>" + filmJson.get(Constant.Movie.AGE_L).asInt() + "</td>");
            filmHtml.append("<td><a href=\"" + FilmRouter.UrlPath.MOVIES + "/" + filmJson.get(Constant.Movie.ID_G).asInt() + "\">Link</a></td>");
            table.append(filmHtml.html());
        }
        return template.html();
    }

    private String getActorNames(List<Actor> actors) {
        var actorsNames = new ArrayList<String>();
        actors.forEach(actor -> actorsNames.add(" " + actor.getName()));
        return View.getCSVFromList(actorsNames.toString());
    }

    public String getMovieHtml(Film movie, List<Actor> cast, List<Comment> comments) throws IOException {
        var template = Jsoup.parse(new File(Constant.Template.MOVIE), "UTF-8");
        var movieJson = JsonHandler.getNodeOfObject(movie);

        //Making the first part which is a list for movie's information
        var listItems = template.select("li");
        List<String> values = Arrays.asList(
                getSingleValue(movieJson, Constant.Movie.NAME),
                getSingleValue(movieJson, Constant.Movie.SUMM),
                getSingleValue(movieJson, Constant.Movie.R_DATE),
                getSingleValue(movieJson, Constant.Movie.DIRECTOR),
                getListValue(movieJson, Constant.Movie.WRITERS),
                getListValue(movieJson, Constant.Movie.GENRE),
                getActorNames(cast),
                getSingleValue(movieJson, Constant.Movie.IMDB),
                getSingleValue(movieJson, Constant.Movie.RATING),
                getSingleValue(movieJson, Constant.Movie.DURATION),
                getSingleValue(movieJson, Constant.Movie.AGE_L)
        );
        loadListElement(listItems, values);

        String baseUrl = FilmRouter.UrlPath.MOVIES + "/" + movie.getId() + "/";

        // Making the add to watch list form
        var watchListForm = template.select("form").get(0);
        watchListForm.attr("action", UserRouter.UrlsPath.W_LIST);
        var movieInput = watchListForm.getElementById("movie_id");
        movieInput.attr("value", movieJson.get(Constant.Movie.ID_G).asText());

        //Making rate movie form
        var rateForm = template.select("form").get(1);
        rateForm.attr("action", baseUrl + FilmRouter.UrlPath.RATE_MOVIE);

        //Making comment form
        var commentTable = template.select("table").first();
        for (var comment : comments) {
            var commentNode = JsonHandler.getNodeOfObject(comment);
            commentTable.append(makeCommentRow(commentNode, CommentRouter.UrlsPath.VCOMMENT));
        }
        return template.html();
    }

    private String getSingleValue(JsonNode node, String key) {
        return node.get(key).asText();
    }

    private String getListValue(JsonNode node, String key) {
        return getCSVFromList(node.get(key).toPrettyString());
    }

    private void loadListElement(Elements listElement, List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            listElement.get(i).append(values.get(i));
        }
    }

    private String makeCommentRow(JsonNode commentNode, String actionUrl) throws IOException {
        var commentHtml = new Element("tr");
        commentHtml.append(makeTableColumn(commentNode.get(Constant.Comment.U_ID).asText()));
        commentHtml.append(makeTableColumn(commentNode.get(Constant.Comment.CONTENT).asText()));
        commentHtml.append(makeTableColumn(commentNode.get(Constant.Comment.LIKES).asText()));
        commentHtml.append(makeTableColumn(commentNode.get(Constant.Comment.DISLIKES).asText()));
        commentHtml.append(makeTableColumn(makeVoteCommentForm(actionUrl, commentNode.get(Constant.Comment.ID).asText())));

        return commentHtml.html();
    }

    private String makeTableColumn(String value) {
        return "<td>" + value + "</td>";
    }

    private String makeVoteCommentForm(String actionUrl, String commentId) throws IOException {
        var template = Jsoup.parse(new File(Constant.Template.VOTE_C_FORM), "UTF-8");
        var commentForm = template.select("form").get(0);
        commentForm.attr("action", actionUrl);
        var commentIdInput = commentForm.getElementById("comment_id");
        commentIdInput.attr("value", commentId);
        return template.html();
    }

}
