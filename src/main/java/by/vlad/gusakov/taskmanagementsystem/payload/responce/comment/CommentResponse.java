package by.vlad.gusakov.taskmanagementsystem.payload.responce.comment;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(
            description = "ID комментария",
            example = "1"
    )
    private Long commentId;

    @Schema(
            description = "Текст комментария",
            example = "Ок! Справлюсь максимально быстро"
    )
    private String text;

    @Schema(
            description = "Дата публикации комментария",
            example = "2023-12-09T11:23:55.952Z"
    )
    private Date createdAt;

    @Schema(
            description = "ID задачи, к которой оставлен комментарий",
            example = "10"
    )
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
