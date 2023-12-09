package by.vlad.gusakov.taskmanagementsystem.exception;

public class CommentNotFoundException extends CustomException{

    public CommentNotFoundException(String title, String detail) {
        super(title, detail);
    }
}
