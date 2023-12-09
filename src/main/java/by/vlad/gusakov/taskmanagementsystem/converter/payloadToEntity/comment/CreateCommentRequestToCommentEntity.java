package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.comment;

import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreateCommentRequestToCommentEntity implements Converter<CreateCommentRequest, Comment> {
    @Override
    public Comment convert(CreateCommentRequest source) {
        Comment comment = new Comment();
        comment.setText(source.getText());

        return comment;
    }
}
