package by.vlad.gusakov.taskmanagementsystem.payload.responce.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    @Schema(
            description = "Название ошибки",
            example = "Ошибка X"
    )
    private String title;

    @Schema(
            description = "Статус ответа от сервера",
            example = "404"
    )
    private int status;

    @Schema(
            description = "Детали ошибки",
            example = "Не удалось выполнить Х, так как Х"
    )
    private String detail;
}
