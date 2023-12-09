package by.vlad.gusakov.taskmanagementsystem.payload.responce.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    private Long id;

    private String title;

    private String description;

    private Task.Status status;

    private Task.Priority priority;

    private Date createdAt;

    private Date updatedAt;

    private Date assignedAt;

    private Long authorId;

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
