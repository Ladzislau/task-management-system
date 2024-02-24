package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.TaskNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.*;
import by.vlad.gusakov.taskmanagementsystem.repository.TaskRepository;
import by.vlad.gusakov.taskmanagementsystem.util.TaskUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserService userService;

    private final ConversionService conversionService;

    private final TaskUtil taskUtil;


    public TaskPageResponse getUserTasks(Long userId, String filterRole, Pageable pageable) {
        User user;
        if (userId == 0) {
            user = userService.getCurrentUser();
        } else {
            user = userService.findById(userId);
        }

        Page<Task> tasks;
        filterRole = filterRole != null ? filterRole.toUpperCase() : "";
        switch (filterRole) {
            case "AUTHOR" -> tasks = taskRepository.findByAuthorOrderByCreatedAtDesc(user, pageable);
            case "ASSIGNEE" -> tasks = taskRepository.findByAssigneeOrderByCreatedAtDesc(user, pageable);
            default -> tasks = taskRepository.findByAuthorOrAssigneeOrderByCreatedAtDesc(user, user, pageable);
        }

        List<TaskResponse> taskResponses = new ArrayList<>();
        tasks.getContent().forEach(c -> taskResponses.add(conversionService.convert(c, TaskResponse.class)));

        return new TaskPageResponse(pageable.getPageNumber(), tasks.getTotalPages(), tasks.getTotalElements(), taskResponses);
    }

    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Ошибка загрузки задачи", "Задачи не существует!"));

        return conversionService.convert(task, TaskResponse.class);
    }

    public CreateTaskResponse createTask(CreateTaskRequest createTaskRequest) {
        User currentUser = userService.getCurrentUser();

        Task task = conversionService.convert(createTaskRequest, Task.class);
        taskUtil.initializeNewTaskFields(task, currentUser, createTaskRequest);

        Task savedTask = taskRepository.save(task);

        return new CreateTaskResponse("Задача успешно создана!",
                conversionService.convert(savedTask, TaskResponse.class));
    }

    public UpdateTaskResponse updateTask(Long taskId, UpdateTaskRequest updateTaskRequest) {
        User currentUser = userService.getCurrentUser();
        Task oldTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Ошибка изменения задачи (id)", "Задача, которую вы хотите изменить, не существует"));

        taskUtil.checkUserPermissionsToModifyTask(currentUser, oldTask);

        Task updatedTask = conversionService.convert(updateTaskRequest, Task.class);
        taskUtil.updateTaskByRole(currentUser, updatedTask, oldTask, updateTaskRequest);

        taskRepository.save(updatedTask);

        return new UpdateTaskResponse("Задача успешно обновлена!", conversionService.convert(updatedTask, TaskResponse.class));
    }

    public DeleteTaskResponse deleteTaskById(Long taskId) {
        User currentUser = userService.getCurrentUser();
        Task taskToDelete = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Ошибка удаления задачи (id)", "Задача, которую вы хотите удалить, не существует!"));

        taskUtil.checkUserPermissionsToDeleteTask(currentUser, taskToDelete);

        taskRepository.deleteById(taskId);

        return new DeleteTaskResponse("Задача удалена!");
    }

}
