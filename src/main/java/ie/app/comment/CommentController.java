package ie.app.comment;

import ie.exception.CustomException;
import ie.generic.controller.Controller;
import io.javalin.http.Context;

import java.io.IOException;

public class CommentController extends Controller {
    private CommentView viewHandler;
    public CommentController() {
        this.viewHandler = new CommentView();
    }
    public void voteCommentHandler(Context ctx) throws CustomException, IOException {
        String userId = null; Integer vote = null;
        var commentId = ctx.pathParamAsClass(CommentRouter.UrlsPath.C_ID, Integer.class).get().toString();
        if (ctx.method() == "GET") {
            userId = ctx.pathParam(CommentRouter.UrlsPath.U_ID);
            vote = ctx.pathParamAsClass(CommentRouter.UrlsPath.VOTE, Integer.class).get();
        } else if(ctx.method() == "POST") {
            userId = ctx.formParam(CommentRouter.UrlsPath.U_ID);
            vote = ctx.formParamAsClass(CommentRouter.UrlsPath.VOTE, Integer.class).get();
        }
        CommentManager.getInstance().voteComment(commentId, userId, vote);
        ctx.html(viewHandler.getSuccessHtmlResponse());
    }
}
