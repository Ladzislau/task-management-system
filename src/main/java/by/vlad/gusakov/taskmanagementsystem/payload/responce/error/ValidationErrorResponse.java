package by.vlad.gusakov.taskmanagementsystem.payload.responce.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class ValidationErrorResponse {
    private String title;

    private int status;

    private Map<String, String> fieldErrors;
}
