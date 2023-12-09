package by.vlad.gusakov.taskmanagementsystem.controller;

import by.vlad.gusakov.taskmanagementsystem.exception.*;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.*;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.*;
import by.vlad.gusakov.taskmanagementsystem.service.CommentService;
import by.vlad.gusakov.taskmanagementsystem.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final CommentService commentService;

    @Autowired
    public TaskController(TaskService taskService, CommentService commentService) {
        this.taskService = taskService;
        this.commentService = commentService;
    }

    @GetMapping("/user-tasks/{userId}")
    public ResponseEntity<TaskPageResponse> getUserTasks(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(value = "filterRole", required = false) String filterRole) throws UserNotFoundException, AuthenticationException {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        TaskPageResponse response = taskService.getUserTasks(userId, filterRole, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable("taskId") Long taskId) throws TaskNotFoundException {

        TaskResponse response = taskService.getTaskById(taskId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest) throws UserNotFoundException, AuthenticationException {

        CreateTaskResponse response = taskService.createTask(createTaskRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<UpdateTaskResponse> updateTask(
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid UpdateTaskRequest updateTaskRequest) throws UserNotFoundException, UnauthorizedModificationException, AuthenticationException, TaskNotFoundException {


        UpdateTaskResponse response = taskService.updateTask(taskId, updateTaskRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<DeleteTaskResponse> deleteTask(
            @PathVariable("taskId") Long taskId) throws UserNotFoundException, TaskNotFoundException, UnauthorizedModificationException, AuthenticationException {

        DeleteTaskResponse response = taskService.deleteTaskById(taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<CommentPageResponse> getTaskComments(
            @PathVariable("taskId") Long taskId,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize) throws TaskNotFoundException {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        CommentPageResponse response = commentService.getCommentsByTask(taskId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CreateCommentResponse> postComment(
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid CreateCommentRequest createCommentRequest) throws TaskNotFoundException {

        CreateCommentResponse response = commentService.postComment(taskId, createCommentRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(
            @PathVariable("taskId") Long taskId, @PathVariable("commentId") Long commentId) throws TaskNotFoundException, CommentNotFoundException {

        CommentResponse response = commentService.getCommentById(taskId, commentId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(
            @PathVariable("commentId") Long commentId, @PathVariable("taskId") Long taskId,
            @RequestBody @Valid UpdateCommentRequest updateCommentRequest) throws CommentNotFoundException, UnauthorizedModificationException, UserNotFoundException, AuthenticationException, TaskNotFoundException {

        UpdateCommentResponse response = commentService.updateComment(taskId, commentId, updateCommentRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @PathVariable("commentId") Long commentId) throws CommentNotFoundException, UnauthorizedModificationException, UserNotFoundException, AuthenticationException {

        DeleteCommentResponse response = commentService.deleteComment(commentId);
        return ResponseEntity.ok(response);
    }

}
