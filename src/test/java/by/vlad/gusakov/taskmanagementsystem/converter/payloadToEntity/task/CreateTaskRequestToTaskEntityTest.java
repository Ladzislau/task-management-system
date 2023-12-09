package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.task;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.model.Task;
import by.vlad.gusakov.taskmanagementsystem.payload.request.task.CreateTaskRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTaskRequestToTaskEntityTest {

    private CreateTaskRequestToTaskEntity converter;

    @BeforeEach
    public void setUp() {
        converter = new CreateTaskRequestToTaskEntity();
    }

    @Test
    public void shouldReturnCommentEntity_whenConvert_giveCreateTaskRequest() {
        // given
        CreateTaskRequest createTaskRequest = TestDataFactory.createCreateTaskRequest();
        Task expected = TestDataFactory.createNewTask();

        // when
        Task result = converter.convert(createTaskRequest);

        // then
        assertThat(result).isEqualTo(expected);
    }

}