package by.vlad.gusakov.taskmanagementsystem.payload.responce.comment;

import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCommentResponse {

    private String message;

    private CommentResponse updatedComment;
}
