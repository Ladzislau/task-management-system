package by.vlad.gusakov.taskmanagementsystem.exception;

import lombok.Getter;

@Getter
public class EmailNotUniqueException extends CustomException{

    public EmailNotUniqueException(String title, String detail) {
        super(title, detail);
    }
}
