package by.vlad.gusakov.taskmanagementsystem.handler;

import by.vlad.gusakov.taskmanagementsystem.exception.*;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException e) {
        HttpStatus status = determineHttpStatus(e);
        ErrorResponse response = new ErrorResponse(e.getTitle(), status.value(), e.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    private HttpStatus determineHttpStatus(CustomException e) {
        if (e instanceof AuthenticationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (e instanceof CommentNotFoundException ||
                e instanceof UserNotFoundException ||
                e instanceof TaskNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (e instanceof EmailNotUniqueException) {
            return HttpStatus.BAD_REQUEST;
        } else if (e instanceof UnauthorizedModificationException) {
            return HttpStatus.FORBIDDEN;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
