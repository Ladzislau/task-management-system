package by.vlad.gusakov.taskmanagementsystem.exception;

public class UserNotFoundException extends CustomException{
    public UserNotFoundException(String title, String detail) {
        super(title, detail);
    }
}
