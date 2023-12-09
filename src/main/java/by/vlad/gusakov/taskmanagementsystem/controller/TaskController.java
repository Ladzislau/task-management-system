package by.vlad.gusakov.taskmanagementsystem.controller;

import by.vlad.gusakov.taskmanagementsystem.exception.*;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.*;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ErrorResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ValidationErrorResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.*;
import by.vlad.gusakov.taskmanagementsystem.service.CommentService;
import by.vlad.gusakov.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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


    @Parameters({
            @Parameter(
                    name = "userId",
                    description = "ID пользователя, задачи которого вы хотите получить",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64")
            ),
            @Parameter(
                    name = "page",
                    description = "номер получаемой страницы задач. Default: 0",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", format = "int32")
            ),
            @Parameter(
                    name = "size",
                    description = "количество задач на странице. Default: 10",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", format = "int32")
            ),
            @Parameter(
                    name = "filterRole",
                    description = "роль, по которой будет происходить фильтрация",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "string", format = "int32",
                            allowableValues = {"author", "assignee"})
            )
    })
    @Operation(
            summary = "Получения задач, связанных с пользователем",
            description = "Задачи другого пользователя может получить любой аутентифицированный пользователь. " +
                    "Полученные задачи можно фильтровать по 2-м критериям, либо же не использовать фильтр" +
                    "\n 1) пользователь является автором задачи " +
                    "\n 2) пользователь является исполнителем задачи",
            responses = {
                    @ApiResponse(
                            description = "Задачи пользователя успешно получены",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskPageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Не удалось получить задачи, пользователя с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
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

    @Parameter(
            name = "taskId",
            description = "ID задачи, которую вы хотите получить",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Получение задачи по ID",
            description = "Получение существующей задачи по её ID",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно получена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable("taskId") Long taskId) throws TaskNotFoundException {

        TaskResponse response = taskService.getTaskById(taskId);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Создание задачи",
            description = "Чтобы создать новую задачу достаточно указать ее название, описание и статус – эти параметры обязательны. " +
                    "Также можно указать ID исполнителя, но этот параметр необязателен",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно создана",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateTaskResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Не удалось создать задачу, поле(-я) запроса содержит(-ат) ошибки",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CreateTaskResponse> createTask(
            @RequestBody @Valid CreateTaskRequest createTaskRequest) throws UserNotFoundException, AuthenticationException {

        CreateTaskResponse response = taskService.createTask(createTaskRequest);
        return ResponseEntity.ok(response);
    }

    @Parameter(
            name = "taskId",
            description = "ID задачи, которую вы хотите обновить",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Обновление задачи",
            description = "Обновление задачи происходит через этот метод, однако его функционал зависит от роли, которую пользователь играет в задаче. " +
                    "Автору задачи доступен полный контроль над задачей, а исполнитель может изменять только статус задачи",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно обновлена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateTaskResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Не удалось обновить задачу, поле(-я) запроса содержит(-ат) ошибки",
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Попытка изменения задачи пользователем, не являющимся ее автором или исполнителем",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/{taskId}")
    public ResponseEntity<UpdateTaskResponse> updateTask(
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid UpdateTaskRequest updateTaskRequest) throws UserNotFoundException, UnauthorizedModificationException, AuthenticationException, TaskNotFoundException {


        UpdateTaskResponse response = taskService.updateTask(taskId, updateTaskRequest);
        return ResponseEntity.ok(response);
    }


    @Parameter(
            name = "taskId",
            description = "ID задачи, которую вы хотите удалить",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Удаление задачи",
            description = "Удаление задачи происходит через этот метод, право удалять задачу имеет ее автор",
            responses = {
                    @ApiResponse(
                            description = "Задача успешно удалена",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeleteTaskResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Попытка удаления задачи пользователем, не являющимся ее автором",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{taskId}")
    public ResponseEntity<DeleteTaskResponse> deleteTask(
            @PathVariable("taskId") Long taskId) throws UserNotFoundException, TaskNotFoundException, UnauthorizedModificationException, AuthenticationException {

        DeleteTaskResponse response = taskService.deleteTaskById(taskId);
        return ResponseEntity.ok(response);
    }

    @Parameters({
            @Parameter(
                    name = "taskId",
                    description = "ID задачи, комментарии которой вы хотите получить",
                    required = true,
                    in = ParameterIn.PATH,
                    schema = @Schema(type = "integer", format = "int64")
            ),
            @Parameter(
                    name = "page",
                    description = "номер получаемой страницы комментариев. Default: 0",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", format = "int32")
            ),
            @Parameter(
                    name = "size",
                    description = "количество комментариев на странице. Default: 10",
                    in = ParameterIn.QUERY,
                    schema = @Schema(type = "integer", format = "int32")
            )
    })
    @Operation(
            summary = "Получение комментариев задачи",
            responses = {
                    @ApiResponse(
                            description = "Комментарии успешно получены",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentPageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<CommentPageResponse> getTaskComments(
            @PathVariable("taskId") Long taskId,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "10") int pageSize) throws TaskNotFoundException {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        CommentPageResponse response = commentService.getCommentsByTask(taskId, pageable);
        return ResponseEntity.ok(response);
    }


    @Parameter(
            name = "taskId",
            description = "ID задачи, комментарий к которой вы хотите опубликовать",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Публикация комментария",
            responses = {
                    @ApiResponse(
                            description = "Комментарий успешно опубликован",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CreateCommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CreateCommentResponse> postComment(
            @PathVariable("taskId") Long taskId,
            @RequestBody @Valid CreateCommentRequest createCommentRequest) throws TaskNotFoundException {

        CreateCommentResponse response = commentService.postComment(taskId, createCommentRequest);
        return ResponseEntity.ok(response);
    }


    @Parameter(
            name = "taskId",
            description = "ID задачи, комментарий к которой вы хотите получить",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Parameter(
            name = "commentId",
            description = "ID комментария, которой вы хотите получить",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Получения комментария по ID",
            responses = {
                    @ApiResponse(
                            description = "Комментарий успешно получен",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи/Комментария с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(
            @PathVariable("taskId") Long taskId, @PathVariable("commentId") Long commentId) throws TaskNotFoundException, CommentNotFoundException {

        CommentResponse response = commentService.getCommentById(taskId, commentId);
        return ResponseEntity.ok(response);
    }


    @Parameter(
            name = "taskId",
            description = "ID задачи, комментарий к которой вы хотите отредактировать",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Parameter(
            name = "commentId",
            description = "ID комментария, который вы хотите отредактировать",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Редактирование комментария",
            responses = {
                    @ApiResponse(
                            description = "Комментарий успешно отредактирован",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UpdateCommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Попытка редактирования комментария пользователем, не являющимся его автором",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи/Комментария с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(
            @PathVariable("commentId") Long commentId, @PathVariable("taskId") Long taskId,
            @RequestBody @Valid UpdateCommentRequest updateCommentRequest) throws CommentNotFoundException, UnauthorizedModificationException, UserNotFoundException, AuthenticationException, TaskNotFoundException {

        UpdateCommentResponse response = commentService.updateComment(taskId, commentId, updateCommentRequest);
        return ResponseEntity.ok(response);
    }

    @Parameter(
            name = "taskId",
            description = "ID задачи, комментарий к которой вы хотите отредактировать",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Parameter(
            name = "commentId",
            description = "ID комментария, которой вы хотите отредактировать",
            required = true,
            in = ParameterIn.PATH,
            schema = @Schema(type = "integer", format = "int64")
    )
    @Operation(
            summary = "Удаление комментария",
            responses = {
                    @ApiResponse(
                            description = "Комментарий успешно удален",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DeleteCommentResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Ошибка аутентификации",
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Попытка удаления комментария пользователем, не являющимся его автором",
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Задачи/Комментария с заданным ID не существует",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{taskId}/comments/{commentId}")
    public ResponseEntity<DeleteCommentResponse> deleteComment(
            @PathVariable("commentId") Long commentId) throws CommentNotFoundException, UnauthorizedModificationException, UserNotFoundException, AuthenticationException {

        DeleteCommentResponse response = commentService.deleteComment(commentId);
        return ResponseEntity.ok(response);
    }

}
