package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.task;

import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UpdateTaskRequestToTaskEntity implements Converter<UpdateTaskRequest, Task> {
    @Override
    public Task convert(UpdateTaskRequest source) {
        Task task = new Task();

        task.setTitle(source.getTitle());
        task.setDescription(source.getDescription());
        task.setPriority(source.getPriority());
        task.setStatus(source.getStatus());

        return task;
    }
}
