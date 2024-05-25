package samleticias.desafiocreaapi.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import samleticias.desafiocreaapi.exceptions.*;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ProfessionalNotFoundException.class)
    public ResponseEntity<StandardError> ProfessionalNotFoundHandler(ProfessionalNotFoundException e, HttpServletRequest request){
        String error = "Profissional não encontrado.";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(TitleNotFoundException.class)
    public ResponseEntity<StandardError> TitleNotFoundHandler(TitleNotFoundException e, HttpServletRequest request){
        String error = "Título não encontrado.";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<StandardError> AlreadyExistHandler(AlreadyExistException e, HttpServletRequest request){
        String error = "Recurso solicitado já existe.";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidActionException.class)
    public ResponseEntity<StandardError> InvalidActionHandler(InvalidActionException e, HttpServletRequest request){
        String error = "Ação inválida.";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<StandardError> DuplicateResourceHandler(DuplicateResourceException e, HttpServletRequest request){
        String error = "Ação solicitada já foi realizada anteriormente.";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(ExistingItemException.class)
    public ResponseEntity<StandardError> ExistingItemHandler(ExistingItemException e, HttpServletRequest request){
        String error = "Atribuição já foi realizada anteriormente.";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
}
