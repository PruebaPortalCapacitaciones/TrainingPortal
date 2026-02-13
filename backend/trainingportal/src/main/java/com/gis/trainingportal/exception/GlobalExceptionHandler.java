package com.gis.trainingportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.gis.trainingportal.common.ApiResponseDto;

/* Clase para manejar excepciones globalmente en la aplicación, proporcionando respuestas consistentes de error */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de argumentos inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                ApiResponseDto.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    // Errores de validación de campos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity<>(
                ApiResponseDto.error(errorMessage, HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST);
    }

    // Errores con código de estado específico
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>(
                ApiResponseDto.error(ex.getReason(), ex.getStatusCode().value()),
                ex.getStatusCode());
    }

    // Errores generales no manejados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Object>> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(
                ApiResponseDto.error("Ocurrió un error inesperado", HttpStatus.INTERNAL_SERVER_ERROR.value()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
