package by.vlad.gusakov.taskmanagementsystem.util;

import by.vlad.gusakov.taskmanagementsystem.exception.UnauthorizedModificationException;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentUtil {

    public void initializeUpdatedCommentFields(Comment updatedComment, Comment oldComment) {
        updatedComment.setId(oldComment.getId());
        updatedComment.setAuthor(oldComment.getAuthor());
    }

    public void checkUserPermissionsToModifyComment(User user, Comment comment) throws UnauthorizedModificationException {
        if(!(comment.getAuthor().equals(user))){
            throw new UnauthorizedModificationException("Ошибка редактирования/удаления комментария", "Вы не можете изменить/удалить данный комментарий, т.к. не являетесь его автором!");
        }
    }
}
