package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.comment;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.payload.request.comment.CreateCommentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateCommentRequestToCommentEntityTest {

    private CreateCommentRequestToCommentEntity converter;

    @BeforeEach
    public void setUp() {
        converter = new CreateCommentRequestToCommentEntity();
    }

    @Test
    public void shouldReturnCommentEntity_whenConvert_giveCreateCommentRequest() {
        // given
        CreateCommentRequest createCommentRequest = TestDataFactory.createCreateCommentRequest();
        Comment expected = TestDataFactory.createNewCommentEntity();

        // when
        Comment result = converter.convert(createCommentRequest);

        // then
        assertThat(result).isEqualTo(expected);
    }

}