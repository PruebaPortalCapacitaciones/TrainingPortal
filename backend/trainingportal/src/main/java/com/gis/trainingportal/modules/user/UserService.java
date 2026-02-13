package com.gis.trainingportal.modules.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gis.trainingportal.security.JwtUtil;

/* Servicio para manejar la lógica relacionada con los usuarios */
@Service
public class UserService {

    private final UserRepo userRepo;
    private final JwtUtil jwtUtil;

    public UserService(UserRepo userRepo, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.jwtUtil = jwtUtil;
    }

    // Registro de nuevo usuario con validaciones
    public Map<String, Object> register(UserEntity user) {

        if (user.getDocument() == null || user.getDocument().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento es obligatorio");
        }
        if (!Pattern.matches("\\d+", user.getDocument())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El documento debe ser numérico");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre es obligatorio");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio");
        }
        if (!Pattern.matches("^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$", user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no tiene un formato válido");
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }
        if (user.getPassword().length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 5 caracteres");
        }

        Optional<UserEntity> existing = userRepo.findByDocument(user.getDocument());
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El usuario con documento " + user.getDocument() + " ya existe");
        }

        UserEntity saved = userRepo.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("document", saved.getDocument());
        response.put("name", saved.getName());
        response.put("email", saved.getEmail());
        response.put("role", saved.getRole());

        return response;
    }

    // Login de usuario existente con validaciones
    public Map<String, Object> login(String document, String password) {
        Optional<UserEntity> userOpt = userRepo.findByDocument(document)
                .filter(u -> u.getPassword().equals(password));

        if (userOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales Inválidas");
        }

        UserEntity user = userOpt.get();

        String token = jwtUtil.generateToken(user.getDocument(), user.getRole());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("document", user.getDocument());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());

        return response;
    }

}
