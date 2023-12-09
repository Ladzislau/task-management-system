package by.vlad.gusakov.taskmanagementsystem.payload.responce.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateCommentResponse {

    private String message;

    private CommentResponse createdComment;

}