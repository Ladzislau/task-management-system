package by.vlad.gusakov.taskmanagementsystem.util;


import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CommentUtilTest {

    private CommentUtil commentUtil;

    @BeforeEach
    void setUp(){
        commentUtil = new CommentUtil();
    }

    @Test
    void initializeUpdatedCommentFields_shouldInitializeCommentIdAndAuthor() {
        Comment updatedComment = new Comment();
        Comment oldComment = new Comment(1L, null, new Date(), new User(), null);

        commentUtil.initializeUpdatedCommentFields(updatedComment, oldComment);

        assert oldComment.getId().equals(updatedComment.getId());
        assert oldComment.getAuthor().equals(updatedComment.getAuthor());
    }

    @Test
    void checkUserPermissionsToModifyComment_shouldCheckPermissionsAndThrowException() throws Exception {
        User authorizedUser = new User();
        authorizedUser.setId(1L);

        User commentAuthor = new User();
        Comment comment = new Comment();
        comment.setAuthor(commentAuthor);

        commentUtil.checkUserPermissionsToModifyComment(commentAuthor, comment);

        assertThrows(UnauthorizedModificationException.class, () ->
                commentUtil.checkUserPermissionsToModifyComment(authorizedUser, comment));
    }
}
