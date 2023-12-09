package by.vlad.gusakov.taskmanagementsystem.exception;

public class TaskNotFoundException extends CustomException{
    public TaskNotFoundException(String title, String detail) {
        super(title, detail);
    }
}
