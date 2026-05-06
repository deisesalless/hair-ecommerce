package br.com.store.hair.exceptions.handle;

import br.com.store.hair.exceptions.BusinessException;
import br.com.store.hair.exceptions.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        log.warn("Validation failed for {}: {}", extractPath(request), ex.getMessage());
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> Optional.ofNullable(error.getDefaultMessage())
                                .orElse("Valor inválido"),
                        (existing, replacement) -> existing
                ));

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
//                ex.getMessage(),
//                extractPath(request),
                fieldErrors
        );
        return new ResponseEntity<>(response, headers, status);
    }

    // Regras de negócio (minha exceção base)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusiness(BusinessException ex, WebRequest request) {
        log.warn("Business rule violated: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                ex.getStatus().value(),
//                ex.getStatus().getReasonPhrase(),
//                request.getDescription(false).replace("uri=", ""),
                Map.of("message", ex.getMessage())
        );
        return new ResponseEntity<>(response, ex.getStatus());
    }

    // Captura qualquer violação de integridade de dados, exceção lançada pelo Spring Data JPA
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        log.error("Data integrity violation: {}", ex.getMessage(), ex);
        String message = translateConstraintViolation(ex);

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
//                HttpStatus.CONFLICT.getReasonPhrase(),
//                request.getDescription(false).replace("uri=", ""),
                Map.of("message", message)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleException(ConstraintViolationException ex, HttpServletRequest request) {
        log.warn("Constraint violation: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.getReasonPhrase(),
//                request.getRequestURI(),
                Map.of("message", ex.getMessage())
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private String translateConstraintViolation(DataIntegrityViolationException ex) {
        String message = ex.getMostSpecificCause().getMessage();
        if (message == null) return "Dados conflitantes. Verifique os valores informados.";

        if (message.contains("UNIQUE") || message.contains("Duplicate entry")) {
            return "Registro duplicado. Verifique se já existe um cadastro com esses dados.";
        }
        if (message.contains("FOREIGN KEY") || message.contains("foreign key")) {
            return "Registro vinculado não encontrado. Verifique as referências.";
        }
        return "Violação de integridade dos dados. Verifique os campos obrigatórios.";
    }
}
