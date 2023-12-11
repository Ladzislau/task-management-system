package by.vlad.gusakov.taskmanagementsystem.payload.responce.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
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
public class TaskResponse {

    @Schema(
            description = "ID задачи",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Заголовок задачи",
            example = "Фикс багов в проекте"
    )
    private String title;

    @Schema(
            description = "Описание задачи",
            example = "Пофиксить баги в проекте"
    )
    private String description;

    @Schema(
            description = "Статус задачи",
            example = "IN_PROGRESS"
    )
    private Task.Status status;

    @Schema(
            description = "Приоритет задачи",
            example = "MEDIUM"
    )
    private Task.Priority priority;

    @Schema(
            description = "Дата публикации комментария",
            example = "2023-12-07T14:28:21.952Z"
    )
    private Date createdAt;

    @Schema(
            description = "Дата обновления комментария",
            example = "2023-12-09T11:23:55.952Z"
    )
    private Date updatedAt;

    @Schema(
            description = "Дата назначения текущего исполнителя задачи",
            example = "2023-12-09T11:23:55.952Z"
    )
    private Date assignedAt;

    @Schema(
            description = "ID автора задачи",
            example = "8"
    )
    private Long authorId;

    @Schema(
            description = "ID исполнителя задачи",
            example = "15"
    )
    private Long assigneeId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TaskResponse that = (TaskResponse) object;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && status == that.status && priority == that.priority && Objects.equals(createdAt, that.createdAt) && Objects.equals(updatedAt, that.updatedAt) && Objects.equals(assignedAt, that.assignedAt) && Objects.equals(authorId, that.authorId) && Objects.equals(assigneeId, that.assigneeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, authorId);
    }
}
