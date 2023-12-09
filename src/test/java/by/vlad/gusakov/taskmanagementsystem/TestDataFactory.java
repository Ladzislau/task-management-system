package by.vlad.gusakov.taskmanagementsystem;

import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.*;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.*;

import java.util.List;

public class TestDataFactory {


    //-----------------------------------ENTITY-------------------------------------

    public static User createNewUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("password");
        return user;
    }

    public static User createAuthenticatedUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("password");
        return user;
    }

    public static Comment createNewCommentEntity() {
        Comment comment = new Comment();
        comment.setText("Test Comment");
        return comment;
    }

    public static Comment createUpdatedCommentEntity() {
        Comment comment = new Comment();
        comment.setText("Updated Comment");
        return comment;
    }

    public static Task createNewTask(){
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description of the task");
        task.setPriority(Task.Priority.HIGH);
        task.setStatus(Task.Status.NEW);
        return task;
    }

    public static Task createUpdatedTask(){
        Task task = new Task();
        task.setTitle("Updated Task");
        task.setDescription("Updated description");
        task.setPriority(Task.Priority.MEDIUM);
        task.setStatus(Task.Status.IN_PROGRESS);
        return task;
    }


    //-----------------------------------REQUEST-------------------------------------

    public static CreateTaskRequest createCreateTaskRequest() {
        return new CreateTaskRequest("Test Task", "Description of the task",
                Task.Priority.HIGH.toValue(), 1L);
    }

    public static UpdateTaskRequest createUpdateTaskRequest() {
        return new UpdateTaskRequest("Updated Task", "Updated description",
                Task.Priority.MEDIUM, Task.Status.IN_PROGRESS, 1L);
    }

    public static CreateCommentRequest createCreateCommentRequest() {
        return new CreateCommentRequest("Test Comment");
    }

    public static UpdateCommentRequest createUpdateCommentRequest() {
        return new UpdateCommentRequest("Updated Comment");
    }

    public static AuthenticationRequest createAuthenticationRequest() {
        return new AuthenticationRequest("test@mail.com", "password");
    }

    public static UserRegistrationRequest createUserRegistrationRequest() {
        return new UserRegistrationRequest("test@mail.com", "John", "Doe", "password");
    }


    //-----------------------------------RESPONSE-------------------------------------

    public static AuthenticationResponse createAuthenticationResponse(){
        return new AuthenticationResponse("jwt-token", 1L);
    }

    public static CreateTaskResponse createCreateTaskResponse(){
        return new CreateTaskResponse("Task was created", new Task());
    }

    public static UpdateTaskResponse createUpdateTaskResponse(){
        return new UpdateTaskResponse("Task was updated", new Task());
    }

    public static DeleteTaskResponse createDeleteTaskResponse(){
        return new DeleteTaskResponse("Task was deleted");
    }

    public static TaskResponse createTaskResponse(){
        return new TaskResponse(new Task());
    }

    public static TaskPageResponse createTaskPageResponse(){
        Task task = new Task();
        task.setStatus(Task.Status.IN_PROGRESS);
        return new TaskPageResponse(1, 5, 50L, List.of(task, new Task()));
    }

    public static CreateCommentResponse createCreateCommentResponse(){
        return new CreateCommentResponse("Comment was created", new Comment());
    }

    public static UpdateCommentResponse createUpdateCommentResponse(){
        return new UpdateCommentResponse("Comment was updated", new Comment());
    }

    public static DeleteCommentResponse createDeleteCommentResponse(){
        return new DeleteCommentResponse("Comment was deleted");
    }

    public static CommentResponse createCommentResponse(){
        return new CommentResponse(new Comment());
    }

    public static CommentPageResponse createCommentPageResponse(){
        return new CommentPageResponse(1, 5, 50L, List.of(new Comment(), new Comment()));
    }

}
