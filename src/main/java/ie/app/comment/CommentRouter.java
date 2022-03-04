package ie.app.comment;

import ie.generic.router.Router;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CommentRouter extends Router {
    private CommentController controller;

    public CommentRouter() { controller = new CommentController(); }
    @Override
    public void addRoutes(Javalin javalin) {
        javalin.routes(() -> {
            path(UrlsPath.VCOMMENT, () -> {
                path(UrlsPath.C_ID, () -> {
                    post(controller::voteCommentHandler);
                });
                path(UrlsPath.U_ID + '/' + UrlsPath.C_ID + '/' + UrlsPath.VOTE, () -> {
                    get(controller::voteCommentHandler);
                });
            });
        });
    }
    public static class UrlsPath {
        public static final String VCOMMENT = "/voteComment";
        public static final String U_ID = "{user_id}";
        public static final String C_ID = "{comment_id}";
        public static final String VOTE = "{vote}";
    }
}
