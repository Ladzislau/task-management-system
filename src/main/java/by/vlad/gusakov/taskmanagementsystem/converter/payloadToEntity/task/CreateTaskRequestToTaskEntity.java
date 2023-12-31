package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.task;

import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateTaskRequestToTaskEntity implements Converter<CreateTaskRequest, Task> {
    @Override
    public Task convert(CreateTaskRequest source) {
        Task task = new Task();

        task.setTitle(source.getTitle());
        task.setDescription(source.getDescription());
        task.setPriority(source.getPriority());
        task.setStatus(Task.Status.NEW);

        return task;
    }
}
