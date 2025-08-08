package com.project.chatapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(UserException e, WebRequest request) {
        ErrorDetail err = ErrorDetail.builder()
                .error(e.getMessage())
                .message(request.getDescription(false))
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetail> messageExceptionHandler(MessageException e, WebRequest request) {
        ErrorDetail err = ErrorDetail.builder()
                .error(e.getMessage())
                .message(request.getDescription(false))
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorDetail> chatExceptionHandler(ChatException e, WebRequest request) {
        ErrorDetail err = ErrorDetail.builder()
                .error(e.getMessage())
                .message(request.getDescription(false))
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, WebRequest request) {
        String error = e.getBindingResult().getFieldError().getDefaultMessage();

        ErrorDetail err = ErrorDetail.builder()
                .error("Validation error")
                .message(error)
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> handlerNoHandlerFoundException(UserException e, WebRequest request) {
        ErrorDetail err = ErrorDetail.builder()
                .error(e.getMessage())
                .message(request.getDescription(false))
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> otherExceptionHandler(Exception e, WebRequest request) {
        ErrorDetail err = ErrorDetail.builder()
                .error(e.getMessage())
                .message(request.getDescription(false))
                .timeStamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

}
