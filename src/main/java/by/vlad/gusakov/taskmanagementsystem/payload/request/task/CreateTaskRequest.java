package by.vlad.gusakov.taskmanagementsystem.payload.request.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateTaskRequest {

    @Schema(
            description = "Заголовок задачи",
            example = "Фикс багов в проекте"
    )
    @NotEmpty(message = "Заголовок задачи не может быть пустым")
    @Size(max = 50, message = "Длина заголовка не может превышать 50 символов")
    private String title;

    @Schema(
            description = "Описание задачи",
            example = "Пофиксить баги в проекте"
    )
    @NotEmpty(message = "Описание задачи не может быть пустым")
    @Size(max = 2500, message = "Описание задачи не может превышать 2500 символов")
    private String description;

    @Schema(
            description = "Приоритет задачи",
            example = "MEDIUM"
    )
    @NotNull(message = "Необходимо указать приоритет задачи")
    @Enumerated(EnumType.STRING)
    private Task.Priority priority;

    @Schema(
            description = "ID исполнителя",
            example = "10"
    )
    @Digits(integer = 19, fraction = 0, message = "Некорректное ID исполнителя")
    private Long assigneeId;

}
