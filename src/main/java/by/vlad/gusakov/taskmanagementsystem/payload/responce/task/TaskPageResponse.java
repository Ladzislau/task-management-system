package by.vlad.gusakov.taskmanagementsystem.payload.responce.task;

import by.vlad.gusakov.taskmanagementsystem.model.Task;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskPageResponse {

    private Integer currentPage;

    private Integer totalPage;

    private Long totalElements;

    private List<Task> tasks;
}
