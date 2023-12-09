package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.comment;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.UpdateCommentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateCommentRequestToCommentEntityTest {

    private UpdateCommentRequestToCommentEntity converter;

    @BeforeEach
    public void setUp() {
        converter = new UpdateCommentRequestToCommentEntity();
    }

    @Test
    public void shouldReturnCommentEntity_whenConvert_giveUpdateCommentRequest() {
        // given
        UpdateCommentRequest updateCommentRequest = TestDataFactory.createUpdateCommentRequest();
        Comment expected = TestDataFactory.createUpdatedCommentEntity();

        // when
        Comment result = converter.convert(updateCommentRequest);

        // then
        assertThat(result).isEqualTo(expected);
    }

}