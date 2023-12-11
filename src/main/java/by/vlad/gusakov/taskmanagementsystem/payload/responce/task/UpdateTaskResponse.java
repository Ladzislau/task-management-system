package by.vlad.gusakov.taskmanagementsystem.payload.responce.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskResponse {

    @Schema(
            description = "Ответ от сервера в виде сообщения",
            example = "Задача успешно обновлена!"
    )
    private String message;

    @Schema(
            description = "Обновленная задача"
    )
    private TaskResponse updatedTask;
}
