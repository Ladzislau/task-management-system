package by.vlad.gusakov.taskmanagementsystem.util;

import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskUtilTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskUtil taskUtil;

    @Test
    void checkUserPermissionsToModifyTask_shouldCheckPermissionsAndThrowException() throws Exception {
        //given
        User authorizedUser = new User();
        authorizedUser.setId(1L);

        User taskAuthor = new User();
        taskAuthor.setId(2L);

        User assignee = new User();
        assignee.setId(3L);

        Task task = new Task();
        task.setAuthor(taskAuthor);
        task.setAssignee(assignee);

        //then
        assertDoesNotThrow(() -> taskUtil.checkUserPermissionsToModifyTask(taskAuthor, task));

        assertDoesNotThrow(() -> taskUtil.checkUserPermissionsToModifyTask(assignee, task));

        assertThrows(UnauthorizedModificationException.class, () -> taskUtil.checkUserPermissionsToModifyTask(authorizedUser, task));
    }

    @Test
    void checkUserPermissionsToDeleteTask_shouldCheckPermissionsAndThrowException() {
        //given
        User authorizedUser = new User();
        authorizedUser.setId(1L);

        User taskAuthor = new User();
        taskAuthor.setId(2L);

        Task task = new Task();
        task.setAuthor(taskAuthor);

        //then
        assertDoesNotThrow(() -> taskUtil.checkUserPermissionsToDeleteTask(taskAuthor, task));

        assertThrows(UnauthorizedModificationException.class, () -> taskUtil.checkUserPermissionsToDeleteTask(authorizedUser, task));
    }

    @Test
    void initializeNewTaskFields_shouldInitializeFieldsCorrectly() throws Exception {
        //given
        Task taskToInitialize = new Task();
        User currentUser = new User();
        User assignee = new User();

        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setAssigneeId(1L);

        //when
        when(userService.findById(anyLong())).thenReturn(assignee);
        taskUtil.initializeNewTaskFields(taskToInitialize, currentUser, createTaskRequest);

        //then
        assertEquals(currentUser, taskToInitialize.getAuthor());
        assertEquals(assignee, taskToInitialize.getAssignee());
        assertEquals(taskToInitialize.getCreatedAt(), taskToInitialize.getAssignedAt());
        assertNotNull(taskToInitialize.getCreatedAt());
        assertNotNull(taskToInitialize.getAssignedAt());
    }

    @Test
    void updateTaskForAuthor_shouldUpdateTaskForAuthorCorrectly() throws Exception {
        //given
        Task oldTask = new Task();
        oldTask.setId(1L);
        oldTask.setComments(Collections.singletonList(new Comment()));
        oldTask.setAuthor(new User());

        Task updatedTask = new Task(2L, "", "", Task.Status.IN_PROGRESS, Task.Priority.LOW, new Date(), null,
                new Date(), Collections.singletonList(new Comment()), new User(), new User());
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        Date updateDate = new Date();

        //when
        taskUtil.updateTaskForAuthor(updatedTask, oldTask, updateTaskRequest, updateDate);

        //then
        assertNotNull(updatedTask.getTitle());
        assertNotNull(updatedTask.getDescription());
        assertNotNull(updatedTask.getStatus());
        assertNotNull(updatedTask.getPriority());
        assertNull(updatedTask.getAssignee());
        assertNull(updatedTask.getAssignedAt());
        assertEquals(oldTask.getId(), updatedTask.getId());
        assertEquals(oldTask.getComments(), updatedTask.getComments());
        assertEquals(oldTask.getAuthor(), updatedTask.getAuthor());
        assertEquals(oldTask.getCreatedAt(), updatedTask.getCreatedAt());
        assertEquals(updateDate, updatedTask.getUpdatedAt());
    }

    @Test
    void updateTaskForAssignee_shouldUpdateOnlyStatus() {
        //given
        Task oldTask = new Task(1L, "", "", Task.Status.NEW, Task.Priority.HIGH, new Date(), null,
                new Date(), Collections.singletonList(new Comment()), new User(), new User());

        Task updatedTask = new Task(2L, null, null, Task.Status.IN_PROGRESS, Task.Priority.LOW, new Date(), null,
                new Date(), Collections.singletonList(new Comment()), new User(), new User());

        Date updateDate = new Date();

        //when
        taskUtil.updateTaskForAssignee(updatedTask, oldTask, updateDate);

        //then
        assertEquals(oldTask.getTitle(), updatedTask.getTitle());
        assertEquals(oldTask.getDescription(), updatedTask.getDescription());
        assertEquals(oldTask.getPriority(), updatedTask.getPriority());
        assertEquals(oldTask.getAssignee(), updatedTask.getAssignee());
        assertEquals(oldTask.getAssignedAt(), updatedTask.getAssignedAt());
        assertEquals(Task.Status.IN_PROGRESS, updatedTask.getStatus());
        assertEquals(oldTask.getId(), updatedTask.getId());
        assertEquals(oldTask.getComments(), updatedTask.getComments());
        assertEquals(oldTask.getAuthor(), updatedTask.getAuthor());
        assertEquals(oldTask.getCreatedAt(), updatedTask.getCreatedAt());
        assertEquals(updateDate, updatedTask.getUpdatedAt());
    }
}
