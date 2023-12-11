package by.vlad.gusakov.taskmanagementsystem.payload.responce.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskPageResponse {

    @Schema(
            description = "Текущая страница",
            example = "1"
    )
    private Integer currentPage;

    @Schema(
            description = "Количество всех страниц",
            example = "5"
    )
    private Integer totalPage;

    @Schema(
            description = "Количество всех задач",
            example = "32"
    )
    private Long totalElements;

    @Schema(
            description = "Полученные задачи"
    )
    private List<TaskResponse> tasks;
}
