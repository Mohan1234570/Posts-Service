package in.krish.binding;

import in.krish.entity.Comment;

public class CommentResponse {

    private Long id;
    private String content;
    private Long userId;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUserId();
    }
}
