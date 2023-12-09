package by.vlad.gusakov.taskmanagementsystem.converter.entityToPayload.task;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.converter.entityToPayload.comment.CommentEntityToCommentResponseConverter;
import by.vlad.gusakov.taskmanagementsystem.model.Comment;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.comment.CommentResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.task.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TaskEntityToTaskResponseConverterTest {

    private TaskEntityToTaskResponseConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new TaskEntityToTaskResponseConverter();
    }

    @Test
    public void shouldReturnTaskResponse_whenConvert_giveCommentEntity() {
        // given
        Date date = new Date();
        Task task = TestDataFactory.createNewTask(date);
        TaskResponse expected = TestDataFactory.createTaskResponse(date);

        // when
        TaskResponse result = converter.convert(task);

        // then
        assertThat(result).isEqualTo(expected);
    }

}