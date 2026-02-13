package com.gis.trainingportal.common;

import lombok.Getter;
import lombok.Setter;

/* DTO para respuestas de API, con campos para éxito, datos, mensaje y código de estado */
@Getter
@Setter
public class ApiResponseDto<T> {

    private boolean success;
    private T data;
    private String message;
    private int code;

    public ApiResponseDto(boolean success, T data, String message, int code) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    // Respuesta de éxito
    public static <T> ApiResponseDto<T> success(String message, T data, int status) {
        return new ApiResponseDto<>(true, data, message, status);
    }

    // Respuesta de error
    public static ApiResponseDto<Object> error(String message, int status) {
        return new ApiResponseDto<>(false, null, message, status);
    }

}
