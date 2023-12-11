package by.vlad.gusakov.taskmanagementsystem.util;


import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CommentUtilTest {

    private CommentUtil commentUtil;

    @BeforeEach
    void setUp(){
        commentUtil = new CommentUtil();
    }

    @Test
    void initializeUpdatedCommentFields_shouldInitializeCommentIdAndAuthor() {
        Comment updatedComment = new Comment();
        Comment oldComment = new Comment(1L, "", new Date(), new User(), new Task());

        commentUtil.initializeUpdatedCommentFields(updatedComment, oldComment);

        assertEquals(oldComment.getId(), updatedComment.getId());
        assertEquals(oldComment.getAuthor(), updatedComment.getAuthor());
        assertNotNull(oldComment.getCreatedAt());
        assertNotNull(oldComment.getRelatedTask());
    }

    @Test
    void checkUserPermissionsToModifyComment_shouldCheckPermissionsAndThrowException(){
        User authorizedUser = new User();
        authorizedUser.setId(1L);

        User commentAuthor = new User();
        Comment comment = new Comment();
        comment.setAuthor(commentAuthor);

        assertDoesNotThrow(() -> commentUtil.checkUserPermissionsToModifyComment(commentAuthor, comment));

        assertThrows(UnauthorizedModificationException.class, () ->
                commentUtil.checkUserPermissionsToModifyComment(authorizedUser, comment));
    }
}
