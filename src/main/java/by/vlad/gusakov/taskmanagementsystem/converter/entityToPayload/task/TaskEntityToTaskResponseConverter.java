package by.vlad.gusakov.taskmanagementsystem.converter.entityToPayload.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.TaskResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityToTaskResponseConverter implements Converter<Task, TaskResponse> {
    @Override
    public TaskResponse convert(Task source) {
        Long assigneeId = null;
        if(source.getAssignee() != null)
            assigneeId = source.getAssignee().getId();

        return new TaskResponse(
                source.getId(), source.getTitle(), source.getDescription(), source.getStatus(),
                source.getPriority(), source.getCreatedAt(), source.getUpdatedAt(), source.getAssignedAt(),
                source.getAuthor().getId(), assigneeId
        );
    }
}
