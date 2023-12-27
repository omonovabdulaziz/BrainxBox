package it.live.brainbox.exceptionHandler;

import it.live.brainbox.exception.AuthException;
import it.live.brainbox.exception.MainException;
import it.live.brainbox.exception.NotFoundException;
import it.live.brainbox.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse authExceptionHandler(AuthException e) {
        return ApiResponse.builder().status(403).message(e.getMessage()).build();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse notFoundException(NotFoundException e) {
        return ApiResponse.builder().status(404).message(e.getMessage()).build();
    }

    @ExceptionHandler(MainException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse primaryException(MainException e) {
        return ApiResponse.builder().status(409).message(e.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        return ApiResponse.builder().object(errors).status(400).build();
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse exceptionAll(Exception e) {
        return ApiResponse
                .builder()
                .message(e.getMessage())
                .status(409)

                .build();
    }

}
