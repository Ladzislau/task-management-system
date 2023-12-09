package by.vlad.gusakov.taskmanagementsystem.converter.entityToPayload.comment;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.comment.CreateCommentRequestToCommentEntity;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.CommentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentEntityToCommentResponseConverterTest {

    private CommentEntityToCommentResponseConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new CommentEntityToCommentResponseConverter();
    }

    @Test
    public void shouldReturnCommentResponse_whenConvert_giveCommentEntity() {
        // given
        Comment comment = TestDataFactory.createCommentEntity();
        CommentResponse expected = TestDataFactory.createCommentResponse();

        // when
        CommentResponse result = converter.convert(comment);

        // then
        assertThat(result).isEqualTo(expected);
    }

}