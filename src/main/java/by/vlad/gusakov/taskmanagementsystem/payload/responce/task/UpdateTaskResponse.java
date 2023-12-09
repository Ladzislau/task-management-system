package by.vlad.gusakov.taskmanagementsystem.payload.responce.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateTaskResponse {

    private String message;

    private TaskResponse updatedTask;
}
