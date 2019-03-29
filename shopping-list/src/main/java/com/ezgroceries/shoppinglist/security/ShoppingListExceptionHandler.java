package com.ezgroceries.shoppinglist.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@ControllerAdvice
@Slf4j
public class ShoppingListExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    //God knows why Spring Security needs this, but otherwise he swallows all my http statuses, even for permitAll URIs
    public ResponseEntity<ExceptionBody> handle(Exception exception) {
        log.error("Translating exception to response", exception);
        ExceptionBody exceptionBody = new ExceptionBody(exception.getMessage());
        return new ResponseEntity<>(exceptionBody, resolveResponseStatus(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionBody> handleException(MethodArgumentNotValidException exception) {
        ExceptionBody exceptionBody = new ExceptionBody(exception.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " " + fe.getDefaultMessage())
                .collect(Collectors.joining(", ")));
        return new ResponseEntity<>(exceptionBody, resolveResponseStatus(exception));
    }

    private HttpStatus resolveResponseStatus(Exception exception) {
        ResponseStatus annotation = findMergedAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ExceptionBody {

        private String message;
    }
}
