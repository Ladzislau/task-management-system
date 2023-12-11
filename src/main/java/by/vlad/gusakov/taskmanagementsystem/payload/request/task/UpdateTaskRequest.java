package by.vlad.gusakov.taskmanagementsystem.payload.request.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskRequest {

    @Schema(
            description = "Обновленный заголовок задачи",
            example = "Фикс багов в проекте X"
    )
    @Size(max = 50, message = "Длина заголовка не может превышать 50 символов")
    private String title;

    @Schema(
            description = "Обновленное описание задачи",
            example = "Пофиксить баг X в функционале проекта X"
    )
    @Size(max = 2500, message = "Описание задачи не может превышать 2500 символов")
    private String description;

    @Schema(
            description = "Обновленный приоритет задачи",
            example = "HIGH"
    )
    @Enumerated(EnumType.STRING)
    private Task.Priority priority;

    @Schema(
            description = "Обновленный статус задачи",
            example = "IN_PROGRESS"
    )
    @Enumerated(EnumType.STRING)
    private Task.Status status;

    @Schema(
            description = "ID исполнителя",
            example = "10"
    )
    @Digits(integer = 19, fraction = 0, message = "Некорректное ID исполнителя")
    private Long assigneeId;
}
