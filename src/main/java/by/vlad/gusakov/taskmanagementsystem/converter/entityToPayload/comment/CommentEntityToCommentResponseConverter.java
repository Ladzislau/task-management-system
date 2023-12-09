package by.vlad.gusakov.taskmanagementsystem.converter.entityToPayload.comment;

import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.CommentResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommentEntityToCommentResponseConverter implements Converter<Comment, CommentResponse> {
    @Override
    public CommentResponse convert(Comment source) {
        return new CommentResponse(source.getId(), source.getText(), source.getCreatedAt(), source.getRelatedTask().getId());
    }
}
