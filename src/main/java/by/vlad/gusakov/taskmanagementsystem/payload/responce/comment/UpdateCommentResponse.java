package by.vlad.gusakov.taskmanagementsystem.payload.responce.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCommentResponse {

    @Schema(
            description = "Ответ от сервера в виде сообщения",
            example = "Комментарий успешно отредактирован!"
    )
    private String message;

    @Schema(
            description = "Отредактированный комментарий"
    )
    private CommentResponse updatedComment;
}
