package ttps.spring.entrega5.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.mail.MessagingException;
import ttps.spring.entrega5.service.AutenticacionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		logger.error("Error de validación:", ex);
		Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(EmptyResultDataAccessException.class) // Ejemplo: excepción específica
	 public ResponseEntity<ErrorResponse> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        logger.error("Recurso no encontrado:", ex);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso no encontrado");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
	
	
	@ExceptionHandler(AutenticacionService.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AutenticacionService.AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("Error de integridad de datos:", ex);
        // Verificar si el error es debido a un DNI duplicado
        if (ex.getMessage().contains("Duplicate entry") ) {
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), "El DNI ingresado ya existe.");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } else {
            // Manejar otras violaciones de integridad de datos
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), "Error de integridad de datos.");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
    }
	
	 @ExceptionHandler(ReferencedWarning.class)
	    public ResponseEntity<ErrorResponse> handleReferencedWarning(ReferencedWarning referencedWarning) {
	        logger.error("Referencia encontrada:", referencedWarning.getMessage());
	        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT.value(), referencedWarning.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
	    }
	 
	 @ExceptionHandler(MessagingException.class)
	    public ResponseEntity<ErrorResponse> handleMessagingException(MessagingException messagingException) {
	        logger.error("Error al enviar el correo:", messagingException);
	        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), messagingException.getMessage());
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	
	 @ExceptionHandler(Exception.class) // Manejador genérico
	    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
	        logger.error("Error no manejado:", ex);
	        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado.");
	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	    }

    // Clase interna estática
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters y setters
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}