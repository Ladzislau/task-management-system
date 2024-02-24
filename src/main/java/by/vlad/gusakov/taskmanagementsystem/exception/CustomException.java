package by.vlad.gusakov.taskmanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends RuntimeException{

    private String title;

    public CustomException(String title, String detail) {
        super(detail);
        this.title = title;
    }
}
