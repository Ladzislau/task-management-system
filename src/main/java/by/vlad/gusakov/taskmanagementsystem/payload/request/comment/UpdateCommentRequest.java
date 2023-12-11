package by.vlad.gusakov.taskmanagementsystem.payload.request.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateCommentRequest {

    @Schema(
            description = "Обновленный текст комментария",
            example = "Ок! Справлюсь максимально быстро"
    )
    @NotEmpty(message = "Комментарий не может быть пустым")
    @Size(max = 400, message = "Длина комментария не может превышать 400 символов")
    private String updatedText;
}
