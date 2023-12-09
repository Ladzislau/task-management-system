package by.vlad.gusakov.taskmanagementsystem.payload.responce.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentResponse {

    private Long commentId;

    private String text;

    private Date createdAt;

    private Long taskId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        CommentResponse that = (CommentResponse) object;
        return Objects.equals(commentId, that.commentId) && Objects.equals(text, that.text) && Objects.equals(createdAt, that.createdAt) && Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, createdAt, taskId);
    }
}
