package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.TaskNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.DeleteTaskResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.TaskPageResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.TaskResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.UpdateTaskResponse;
import by.vlad.gusakov.taskmanagementsystem.repository.TaskRepository;
import by.vlad.gusakov.taskmanagementsystem.util.TaskUtil;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private ConversionService conversionService;

    @MockBean
    private TaskUtil taskUtil;

    private TaskService taskService;

    private User testUser;
    private List<Task> testTasks;


    @BeforeEach
    void setUp() {
        this.taskService = new TaskService(taskRepository, userService, conversionService, taskUtil);

        testUser = new User();
        testUser.setId(1L);

        testTasks = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            Task task = new Task();
            task.setId(i);
            testTasks.add(task);
        }
    }

    @Test
    void getUserTasks_filterByAuthor_shouldReturnTaskListResponse() throws Exception {
        Long userId = 1L;
        String filterRole = "author";
        Pageable pageable = mock(Pageable.class);
        Page<Task> page = new PageImpl<>(testTasks);

        when(userService.findById(userId)).thenReturn(testUser);
        when(taskRepository.findByAuthorOrderByCreatedAtDesc(testUser, pageable)).thenReturn(page);

        TaskPageResponse response = taskService.getUserTasks(userId, filterRole, pageable);

        assertEquals(pageable.getPageNumber(), response.getCurrentPage());
        assertEquals(page.getTotalElements(), response.getTotalElements());
        assertEquals(testTasks.size(), response.getTasks().size());
    }

    @Test
    void getUserTasks_filterByAssignee_shouldReturnTaskListResponse() throws Exception {
        String filterRole = "assignee";
        Pageable pageable = mock(Pageable.class);
        Page<Task> page = new PageImpl<>(testTasks);

        when(userService.getCurrentUser()).thenReturn(testUser);
        when(taskRepository.findByAssigneeOrderByCreatedAtDesc(testUser, pageable)).thenReturn(page);

        TaskPageResponse response = taskService.getUserTasks(null, filterRole, pageable);

        assertEquals(pageable.getPageNumber(), response.getCurrentPage());
        assertEquals(page.getTotalElements(), response.getTotalElements());
        assertEquals(testTasks.size(), response.getTasks().size());
    }

    @Test
    void getUserTasks_defaultFilter_shouldReturnTaskListResponse() throws Exception {
        Pageable pageable = mock(Pageable.class);
        Page<Task> page = new PageImpl<>(testTasks);

        when(userService.getCurrentUser()).thenReturn(testUser);
        when(taskRepository.findByAuthorOrAssigneeOrderByCreatedAtDesc(testUser, testUser, pageable)).thenReturn(page);

        TaskPageResponse response = taskService.getUserTasks(null, "otherRole", pageable);

        assertEquals(pageable.getPageNumber(), response.getCurrentPage());
        assertEquals(page.getTotalElements(), response.getTotalElements());
        assertEquals(testTasks.size(), response.getTasks().size());
    }

    @Test
    void getTaskById_taskExists_shouldReturnTaskResponseWithInitializedComments() throws Exception {
        Long taskId = 1L;
        Task mockTask = new Task();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));

        TaskResponse taskResponse = taskService.getTaskById(taskId);

        assertNotNull(taskResponse);
        assertEquals(mockTask, taskResponse.getTask());
        assertTrue(Hibernate.isInitialized(mockTask.getComments()));
    }

    @Test
    void getTaskById_taskNotFound_shouldThrowTaskNotFoundException() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void updateTask_validInput_shouldReturnUpdateTaskResponse() throws Exception {
        Long taskId = 1L;
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        Task oldTask = new Task();
        Task updatedTask = new Task();

        when(userService.getCurrentUser()).thenReturn(testUser);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));
        when(conversionService.convert(updateTaskRequest, Task.class)).thenReturn(updatedTask);

        UpdateTaskResponse updateTaskResponse = taskService.updateTask(taskId, updateTaskRequest);

        assertNotNull(updateTaskResponse);
        assertEquals("Задача успешно обновлена!", updateTaskResponse.getMessage());
        assertEquals(updatedTask, updateTaskResponse.getUpdatedTask());
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void updateTask_unauthorizedModification_shouldThrowUnauthorizedModificationException() throws Exception {
        Long taskId = 1L;
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest(); // Provide valid update request
        Task oldTask = new Task(); // Provide an old task
        when(userService.getCurrentUser()).thenReturn(new User()); // Provide a user
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));
        doThrow(new UnauthorizedModificationException("Unauthorized modification", "Unauthorized modification")).when(taskUtil).checkUserPermissionsToModifyTask(any(), any());

        assertThrows(UnauthorizedModificationException.class, () -> taskService.updateTask(taskId, updateTaskRequest));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void deleteTaskById_validInput_shouldReturnDeleteTaskResponse() throws Exception {
        Long taskId = 1L;
        Task taskToDelete = new Task();
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskToDelete));

        DeleteTaskResponse deleteTaskResponse = taskService.deleteTaskById(taskId);

        assertNotNull(deleteTaskResponse);
        assertEquals("Задача удалена!", deleteTaskResponse.getMessage());
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTaskById_unauthorizedDeletion_shouldThrowUnauthorizedModificationException() throws Exception {
        Long taskId = 1L;
        Task taskToDelete = new Task();

        when(userService.getCurrentUser()).thenReturn(new User());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskToDelete));
        doThrow(new UnauthorizedModificationException("Unauthorized modification", "Unauthorized modification")).when(taskUtil).checkUserPermissionsToDeleteTask(any(), any());

        assertThrows(UnauthorizedModificationException.class, () -> taskService.deleteTaskById(taskId));
        verify(taskRepository, never()).deleteById(any());
    }
}