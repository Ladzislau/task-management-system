package by.vlad.gusakov.taskmanagementsystem.payload.responce.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse {

    @Schema(
            description = "Название ошибки",
            example = "Ошибка валидации"
    )
    private String title;

    @Schema(
            description = "Название ошибки",
            example = "400"
    )
    private int status;

    @Schema(
            description = "Поля, которые не прошли валидацию",
            example = "{ \"field1\": \"Ошибка валидации Х\", \"field2\": \"Ошибка валидации Х\" }"
    )
    private Map<String, String> fieldErrors;
}
