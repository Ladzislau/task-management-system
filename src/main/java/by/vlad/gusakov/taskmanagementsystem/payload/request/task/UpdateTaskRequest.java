package by.vlad.gusakov.taskmanagementsystem.payload.request.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
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

    @Size(max = 50, message = "Длина заголовка не может превышать 50 символов")
    private String title;

    @Size(max = 2500, message = "Описание задачи не может превышать 2500 символов")
    private String description;

    @Enumerated(EnumType.STRING)
    private Task.Priority priority;

    @Enumerated(EnumType.STRING)
    private Task.Status status;

    @Digits(integer = 19, fraction = 0, message = "Некорректное ID исполнителя")
    private Long assigneeId;
}
