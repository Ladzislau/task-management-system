package by.vlad.gusakov.taskmanagementsystem.util;

import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TaskUtil {

    private final UserService userService;

    @Autowired
    public TaskUtil(UserService userService) {
        this.userService = userService;
    }

    public void checkUserPermissionsToModifyTask(User user, Task task) throws UnauthorizedModificationException {
        if (!(user.equals(task.getAuthor()) || user.equals(task.getAssignee()))) {
            throw new UnauthorizedModificationException("Ошибка изменения задачи", "Вы не можете изменить задачу, " +
                    "т.к. не являетесь её автором или исполнителем!");
        }
    }

    public void checkUserPermissionsToDeleteTask(User user, Task task) throws UnauthorizedModificationException {
        if (!(user.equals(task.getAuthor()))) {
            throw new UnauthorizedModificationException("Ошибка удаления задачи", "Вы не можете удалить задачу, " +
                    "т.к. не являетесь её автором!");
        }
    }

    public void updateTaskByRole(User currentUser, Task updatedTask, Task oldTask, UpdateTaskRequest updateTaskRequest) throws UserNotFoundException {
        Date updateDate = new Date();
        if (currentUser.equals(oldTask.getAuthor())) {
            updateTaskForAuthor(updatedTask, oldTask, updateTaskRequest, updateDate);
        } else {
            updateTaskForAssignee(updatedTask, oldTask, updateDate);
        }
    }

    public void initializeNewTaskFields(Task taskToInitialize, User currentUser, CreateTaskRequest createTaskRequest) throws UserNotFoundException {
        Date creationDate = new Date();

        taskToInitialize.setAuthor(currentUser);
        taskToInitialize.setCreatedAt(creationDate);

        Long assigneeId = createTaskRequest.getAssigneeId();
        taskToInitialize.setAssignee(assigneeId != null ? userService.findById(assigneeId) : null);
        taskToInitialize.setAssignedAt(taskToInitialize.getAssignee() != null ? creationDate : null);
    }

    protected void updateTaskForAuthor(Task updatedTask, Task oldTask, UpdateTaskRequest updateTaskRequest, Date updateDate) throws UserNotFoundException {
        updatedTask.setTitle(updatedTask.getTitle() != null ? updatedTask.getTitle() : oldTask.getTitle());
        updatedTask.setDescription(updatedTask.getDescription() != null ? updatedTask.getDescription() : oldTask.getDescription());
        updatedTask.setStatus(updatedTask.getStatus() != null ? updatedTask.getStatus() : oldTask.getStatus());
        updatedTask.setPriority(updatedTask.getPriority() != null ? updatedTask.getPriority() : oldTask.getPriority());

        Long assigneeId = updateTaskRequest.getAssigneeId();
        updatedTask.setAssignee(assigneeId != null ? userService.findById(assigneeId) : oldTask.getAssignee());
        updatedTask.setAssignedAt(assigneeId != null ? updateDate : oldTask.getAssignedAt());

        updatedTask.setId(oldTask.getId());
        updatedTask.setComments(oldTask.getComments());
        updatedTask.setAuthor(oldTask.getAuthor());
        updatedTask.setCreatedAt(oldTask.getCreatedAt());
        updatedTask.setUpdatedAt(updateDate);
    }

    protected void updateTaskForAssignee(Task updatedTask, Task oldTask, Date updateDate) {
        updatedTask.setTitle(oldTask.getTitle());
        updatedTask.setDescription(oldTask.getDescription());
        updatedTask.setPriority(oldTask.getPriority());
        updatedTask.setAssignee(oldTask.getAssignee());
        updatedTask.setAssignedAt(oldTask.getAssignedAt());

        updatedTask.setStatus(updatedTask.getStatus() != null ? updatedTask.getStatus() : oldTask.getStatus());

        updatedTask.setId(oldTask.getId());
        updatedTask.setComments(oldTask.getComments());
        updatedTask.setAuthor(oldTask.getAuthor());
        updatedTask.setCreatedAt(oldTask.getCreatedAt());
        updatedTask.setUpdatedAt(updateDate);
    }

}
