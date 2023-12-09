package by.vlad.gusakov.taskmanagementsystem.payload.request.comment;

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

    @NotEmpty(message = "Комментарий не может быть пустым")
    @Size(max = 400, message = "Длина комментария не может превышать 400 символов")
    private String text;
}
