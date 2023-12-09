package by.vlad.gusakov.taskmanagementsystem.handler;

import by.vlad.gusakov.taskmanagementsystem.exception.AuthenticationException;
import by.vlad.gusakov.taskmanagementsystem.exception.TaskNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionHandlerTest {

    private final CustomExceptionHandler exceptionHandler = new CustomExceptionHandler();

    @Test
    void handleAuthenticationException_shouldReturnUnauthorized() {
        AuthenticationException authenticationException = new AuthenticationException("Authentication error", "Invalid credentials");

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleException(authenticationException);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("Authentication error", responseEntity.getBody().getTitle());
        assertEquals("Invalid credentials", responseEntity.getBody().getDetail());
    }

    @Test
    void handleTaskNotFoundException_shouldReturnNotFound() {
        TaskNotFoundException taskNotFoundException = new TaskNotFoundException("Task not found", "The requested task does not exist");

        ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.handleException(taskNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Task not found", responseEntity.getBody().getTitle());
        assertEquals("The requested task does not exist", responseEntity.getBody().getDetail());
    }

}
