package by.vlad.gusakov.taskmanagementsystem.exception;

public class AuthenticationException extends CustomException{

    public AuthenticationException(String title, String detail) {
        super(title, detail);
    }
}
