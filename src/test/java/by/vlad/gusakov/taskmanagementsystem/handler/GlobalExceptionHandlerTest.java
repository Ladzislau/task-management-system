package by.vlad.gusakov.taskmanagementsystem.handler;

import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleValidationException_shouldReturnValidationErrorResponse() {
        MethodArgumentNotValidException validationException = createValidationException();

        ResponseEntity<ValidationErrorResponse> responseEntity = exceptionHandler.handleValidationException(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Ошибка валидации", responseEntity.getBody().getTitle());
        assertEquals(1, responseEntity.getBody().getFieldErrors().size());
        assertEquals("Error message", responseEntity.getBody().getFieldErrors().get("field1"));
    }

    private MethodArgumentNotValidException createValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = Collections.singletonList(new FieldError("objectName", "field1", "Error message"));

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        return new MethodArgumentNotValidException(null, bindingResult);
    }

    @Test
    void handleValidationException_withMultipleErrors_shouldReturnValidationErrorResponse() {
        MethodArgumentNotValidException validationException = createValidationExceptionWithMultipleErrors();

        ResponseEntity<ValidationErrorResponse> responseEntity = exceptionHandler.handleValidationException(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Ошибка валидации", responseEntity.getBody().getTitle());
        assertEquals(2, responseEntity.getBody().getFieldErrors().size());
        assertEquals("Error message 1", responseEntity.getBody().getFieldErrors().get("field1"));
        assertEquals("Error message 2", responseEntity.getBody().getFieldErrors().get("field2"));
    }

    private MethodArgumentNotValidException createValidationExceptionWithMultipleErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = List.of(
                new FieldError("objectName", "field1", "Error message 1"),
                new FieldError("objectName", "field2", "Error message 2"));

        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        return new MethodArgumentNotValidException(null, bindingResult);
    }
}
