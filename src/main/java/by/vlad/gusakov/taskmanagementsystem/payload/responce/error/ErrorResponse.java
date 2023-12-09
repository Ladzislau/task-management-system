package by.vlad.gusakov.taskmanagementsystem.payload.responce.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {

    private String title;

    private int status;

    private String detail;
}
