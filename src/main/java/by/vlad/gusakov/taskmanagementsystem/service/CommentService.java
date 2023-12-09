package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.*;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.*;
import by.vlad.gusakov.taskmanagementsystem.repository.CommentRepository;
import by.vlad.gusakov.taskmanagementsystem.repository.TaskRepository;
import by.vlad.gusakov.taskmanagementsystem.util.CommentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final CommentUtil commentUtil;

    private final UserService userService;

    private final ConversionService conversionService;

    @Autowired
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, CommentUtil commentUtil, UserService userService, ConversionService conversionService) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.commentUtil = commentUtil;
        this.userService = userService;
        this.conversionService = conversionService;
    }

    public CreateCommentResponse postComment(Long taskId, CreateCommentRequest createCommentRequest) throws TaskNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Ошибка публикации комментария", "Задача, к которой вы хотите оставить комментарий не существует!"));

        Comment comment = conversionService.convert(createCommentRequest, Comment.class);
        comment.setCreatedAt(new Date());
        comment.setRelatedTask(task);

        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse("Комментарий успешно опубликован!",
                conversionService.convert(savedComment, CommentResponse.class));
    }

    public UpdateCommentResponse updateComment(Long taskId, Long commentId, UpdateCommentRequest updateCommentRequest) throws CommentNotFoundException, UnauthorizedModificationException, UserNotFoundException, AuthenticationException, TaskNotFoundException {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Ошибка редактирования комментария", "Задача, комментарий к которой вы отредактировать обновить, не существует!"));

        User currentUser = userService.getCurrentUser();
        Comment oldComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Ошибка редактирования комментария", "Комментарий, который вы хотите изменить, не существует!"));

        commentUtil.checkUserPermissionsToModifyComment(currentUser, oldComment);

        Comment updatedComment = conversionService.convert(updateCommentRequest, Comment.class);
        commentUtil.initializeUpdatedCommentFields(updatedComment, oldComment);

        commentRepository.save(updatedComment);

        return new UpdateCommentResponse("Комментарий успешно отредактирован!",
                conversionService.convert(updatedComment, CommentResponse.class));
    }

    public DeleteCommentResponse deleteComment(Long commentId) throws CommentNotFoundException, UnauthorizedModificationException, UserNotFoundException, AuthenticationException {
        User currentUser = userService.getCurrentUser();
        Comment commentToDelete = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Ошибка удаления комментария", "Комментарий, который вы хотите удалить, не существует!"));

        commentUtil.checkUserPermissionsToModifyComment(currentUser, commentToDelete);

        commentRepository.deleteById(commentId);

        return new DeleteCommentResponse("Комментарий удален!");
    }

    public CommentResponse getCommentById(Long taskId, Long commentId) throws TaskNotFoundException, CommentNotFoundException {
        taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Невозможно загрузить комментарий", "Задача, для которой вы хотите загрузить комментарий, не существует!"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Невозможно загрузить комментарий (id)", "Комментария не существует!"));
        return conversionService.convert(comment, CommentResponse.class);
    }

    public CommentPageResponse getCommentsByTask(Long taskId, Pageable pageable) throws TaskNotFoundException {
        Task relatedTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Невозможно загрузить комментарии", "Задача, для которой вы хотите загрузить комментарии, не существует!"));

        Page<Comment> comments = commentRepository.findByRelatedTaskOrderByCreatedAtDesc(relatedTask, pageable);

        List<CommentResponse> commentResponses = new ArrayList<>();
        comments.getContent().forEach(c -> commentResponses.add(conversionService.convert(c, CommentResponse.class)));

        return new CommentPageResponse(pageable.getPageNumber(), comments.getTotalPages(),
                comments.getTotalElements(), commentResponses);
    }




}
