package info.novatec.spring.showcase.storage.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

  @ExceptionHandler({
    IllegalArgumentException.class,
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<String> badRequest(Exception ex) {
    LOGGER.warn("Invalid request", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> badRequest(MethodArgumentNotValidException ex) {
    List<String> invalidArguments =
        ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getField)
            .collect(Collectors.toList());
    LOGGER.warn("Invalid arguments: {}", invalidArguments);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<String> unsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
    LOGGER.error("Unsupported media type", ex);
    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> forbidden(Exception ex) {
    LOGGER.error("Access Denied", ex);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  public ResponseEntity<String> conflict(OptimisticLockingFailureException ex) {
    LOGGER.warn("Optimistic locking", ex);
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> internalServerError(Exception ex) {
    LOGGER.error("An error occured", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
