package in.krish.binding;

import in.krish.entity.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String content;
    private Long userId;
    private String userEmail;
    private LocalDateTime createdAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.userId = comment.getUserId();
        this.userEmail = comment.getUserEmail();
        this.createdAt = comment.getCreatedAt();
    }
}
