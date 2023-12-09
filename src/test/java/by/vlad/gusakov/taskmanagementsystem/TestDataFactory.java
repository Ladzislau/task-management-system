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

import java.util.Date;
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

    public static Comment createCommentEntity() {
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId(2L);

        return new Comment(1L, "text", null, user, task);
    }

    public static Task createNewTask(){
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Description of the task");
        task.setStatus(Task.Status.NEW);
        task.setPriority(Task.Priority.HIGH);

        return task;
    }

    public static Task createNewTask(Date date){
        User author = new User();
        author.setId(2L);

        User assignee = new User();
        assignee.setId(3L);

        return new Task(1L, "", "", Task.Status.NEW, Task.Priority.HIGH, date, date, date, null,author, assignee);
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
                Task.Priority.HIGH, 1L);
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
        return new CreateTaskResponse("Task was created", new TaskResponse());
    }

    public static UpdateTaskResponse createUpdateTaskResponse(){
        return new UpdateTaskResponse("Task was updated", new TaskResponse());
    }

    public static DeleteTaskResponse createDeleteTaskResponse(){
        return new DeleteTaskResponse("Task was deleted");
    }

    public static TaskResponse createTaskResponse(Date date){
        return new TaskResponse(1L, "", "", Task.Status.NEW, Task.Priority.HIGH, date, date, date, 2L, 3L);
    }

    public static TaskPageResponse createTaskPageResponse(){
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setStatus(Task.Status.IN_PROGRESS);
        return new TaskPageResponse(1, 5, 50L, List.of(taskResponse, new TaskResponse()));
    }

    public static CreateCommentResponse createCreateCommentResponse(){
        return new CreateCommentResponse("Comment was created", new CommentResponse());
    }

    public static UpdateCommentResponse createUpdateCommentResponse(){
        return new UpdateCommentResponse("Comment was updated", new CommentResponse());
    }

    public static DeleteCommentResponse createDeleteCommentResponse(){
        return new DeleteCommentResponse("Comment was deleted");
    }

    public static CommentResponse createCommentResponse(){
        return new CommentResponse(1L, "text", null, 2L);
    }

    public static CommentPageResponse createCommentPageResponse(){
        return new CommentPageResponse(1, 5, 50L, List.of(new CommentResponse(), new CommentResponse()));
    }

}
