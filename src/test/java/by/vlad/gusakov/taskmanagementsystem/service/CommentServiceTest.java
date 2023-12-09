package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.CommentNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.exception.TaskNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.*;
import by.vlad.gusakov.taskmanagementsystem.repository.CommentRepository;
import by.vlad.gusakov.taskmanagementsystem.repository.TaskRepository;
import by.vlad.gusakov.taskmanagementsystem.util.CommentUtil;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private CommentUtil commentUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private ConversionService conversionService;

    private CommentService commentService;

    private Task testTask;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        this.commentService = new CommentService(commentRepository, taskRepository, commentUtil, userService, conversionService);

        testTask = new Task();
        testTask.setId(1L);

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setRelatedTask(testTask);
    }

    @Test
    void postComment_validTaskIdAndRequest_shouldCreateComment() throws Exception {
        Long taskId = 1L;
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(conversionService.convert(createCommentRequest, Comment.class)).thenReturn(testComment);
        when(commentRepository.save(testComment)).thenReturn(testComment);

        CreateCommentResponse response = commentService.postComment(taskId, createCommentRequest);

        assertNotNull(response);
        assertEquals("Комментарий успешно опубликован!", response.getMessage());
    }

    @Test
    void postComment_invalidTaskId_shouldThrowTaskNotFoundException() {
        Long taskId = 1L;
        CreateCommentRequest createCommentRequest = new CreateCommentRequest();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.postComment(taskId, createCommentRequest));
    }

    @Test
    void updateComment_validCommentIdAndRequest_shouldUpdateComment() throws Exception {
        Long commentId = 1L;
        Long taskId = 1L;
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        Comment oldComment = new Comment();
        Comment updatedComment = new Comment();
        User currentUser = new User();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(oldComment));
        when(conversionService.convert(updateCommentRequest, Comment.class)).thenReturn(updatedComment);

        UpdateCommentResponse response = commentService.updateComment(taskId, commentId, updateCommentRequest);

        assertNotNull(response);
        assertEquals("Комментарий успешно отредактирован!", response.getMessage());
    }

    @Test
    void updateComment_invalidCommentId_shouldThrowCommentNotFoundException() {
        Long commentId = 1L;
        Long taskId = 1L;
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(taskId, commentId, updateCommentRequest));
    }

    @Test
    void updateComment_invalidTaskId_shouldThrowTaskNotFoundException() {
        Long commentId = 1L;
        Long taskId = 1000L;
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.updateComment(taskId, commentId, updateCommentRequest));
    }

    @Test
    void updateComment_unauthorizedModification_shouldThrowUnauthorizedModificationException() throws Exception {
        Long commentId = 1L;
        Long taskId = 1L;
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();
        User currentUser = new User();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));
        doThrow(UnauthorizedModificationException.class).when(commentUtil).checkUserPermissionsToModifyComment(currentUser, testComment);

        assertThrows(UnauthorizedModificationException.class, () -> commentService.updateComment(taskId, commentId, updateCommentRequest));
    }

    @Test
    void deleteComment_validCommentId_shouldDeleteComment() throws Exception {
        Long commentId = 1L;
        User currentUser = new User();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));

        DeleteCommentResponse response = commentService.deleteComment(commentId);

        assertNotNull(response);
        assertEquals("Комментарий удален!", response.getMessage());
    }

    @Test
    void deleteComment_invalidCommentId_shouldThrowCommentNotFoundException() {
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(commentId));
    }

    @Test
    void deleteComment_unauthorizedModification_shouldThrowUnauthorizedModificationException() throws Exception {
        Long commentId = 1L;
        Comment commentToDelete = new Comment();
        User currentUser = new User();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentToDelete));
        doThrow(UnauthorizedModificationException.class).when(commentUtil).checkUserPermissionsToModifyComment(currentUser, commentToDelete);
        assertThrows(UnauthorizedModificationException.class, () -> commentService.deleteComment(commentId));
    }

    @Test
    void getCommentById_shouldReturnCommentResponse() throws TaskNotFoundException, CommentNotFoundException {
        Long taskId = 1L;
        Long commentId = 1L;
        CommentResponse expectedResponse = new CommentResponse(1L, "test", new Date(), taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));
        when(conversionService.convert(testComment, CommentResponse.class)).thenReturn(expectedResponse);

        CommentResponse response = commentService.getCommentById(taskId, commentId);

        assertEquals(expectedResponse.getCommentId(), response.getCommentId());
        assertEquals(expectedResponse.getText(), response.getText());
    }

    @Test
    void getCommentById_taskNotFound_shouldThrowTaskNotFoundException() {
        Long taskId = 1L;
        Long commentId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.getCommentById(taskId, commentId));
        verify(commentRepository, never()).findById(commentId);
    }

    @Test
    void getCommentById_commentNotFound_shouldThrowCommentNotFoundException() {
        Long taskId = 1L;
        Long commentId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(taskId, commentId));
    }

    @Test
    void getCommentsByTask_shouldReturnCommentListResponse() throws TaskNotFoundException {
        Long taskId = 1L;
        List<Comment> testComments = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            Comment comment = new Comment();
            comment.setId(i);
            comment.setRelatedTask(testTask);
            testComments.add(comment);
        }
        Pageable pageable = mock(Pageable.class);
        Page<Comment> page = new PageImpl<>(testComments);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(testTask));
        when(commentRepository.findByRelatedTaskOrderByCreatedAtDesc(testTask, pageable)).thenReturn(page);

        CommentPageResponse response = commentService.getCommentsByTask(taskId, pageable);

        assertEquals(pageable.getPageNumber(), response.getCurrentPage());
        assertEquals(page.getTotalElements(), response.getTotalElements());
        assertEquals(testComments.size(), response.getComments().size());
    }

    @Test
    void getCommentsByTask_taskNotFound_shouldThrowTaskNotFoundException() {
        Long taskId = 1L;
        Pageable pageable = mock(Pageable.class);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.getCommentsByTask(taskId, pageable));
    }

}