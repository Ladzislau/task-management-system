package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.task;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.UpdateTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateTaskRequestToTaskEntityTest {

    private UpdateTaskRequestToTaskEntity converter;

    @BeforeEach
    public void setUp() {
        converter = new UpdateTaskRequestToTaskEntity();
    }

    @Test
    public void shouldReturnCommentEntity_whenConvert_giveUpdateTaskRequest() {
        // given
        UpdateTaskRequest createTaskRequest = TestDataFactory.createUpdateTaskRequest();
        Task expected = TestDataFactory.createUpdatedTask();

        // when
        Task result = converter.convert(createTaskRequest);

        // then
        assertThat(result).isEqualTo(expected);
    }


}