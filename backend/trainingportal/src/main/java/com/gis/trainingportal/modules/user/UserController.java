package com.gis.trainingportal.modules.user;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gis.trainingportal.common.ApiResponseDto;

/* Controlador para manejar las operaciones relacionadas con los usuarios */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registro de nuevo usuario
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> register(@RequestBody UserEntity user) {
        Map<String, Object> saved = userService.register(user);
        return ResponseEntity.ok(ApiResponseDto.success("Usuario registrado con éxito", saved, 200));
    }

    // Login de usuario existente
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> login(
            @RequestParam String document,
            @RequestParam String password) {

        Map<String, Object> loginResponse = userService.login(document, password);

        return ResponseEntity.ok(ApiResponseDto.success("Login Exitoso", loginResponse, 200));
    }

}
