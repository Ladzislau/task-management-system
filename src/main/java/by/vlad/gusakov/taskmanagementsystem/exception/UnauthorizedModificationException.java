package by.vlad.gusakov.taskmanagementsystem.exception;

public class UnauthorizedModificationException extends CustomException {
    public UnauthorizedModificationException(String title, String detail) {
        super(title, detail);
    }
}
