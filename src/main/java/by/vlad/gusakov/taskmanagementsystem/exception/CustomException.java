package by.vlad.gusakov.taskmanagementsystem.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception{

    private String title;

    public CustomException(String title, String detail) {
        super(detail);
        this.title = title;
    }
}
