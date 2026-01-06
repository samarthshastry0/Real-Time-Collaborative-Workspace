package com.samarthshastry.workspace.backend.controller;

import com.samarthshastry.workspace.backend.model.User;
import com.samarthshastry.workspace.backend.service.UserService;
import com.samarthshastry.workspace.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getEmail(), request.getPassword());
            String token = jwtUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(Map.of("token", token, "user", user));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean authenticated = userService.authenticateUser(request.getEmail(), request.getPassword());
        if (authenticated) {
            String token = jwtUtil.generateToken(request.getEmail());
            return userService.getUserByEmail(request.getEmail())
                    .map(user -> ResponseEntity.ok(Map.of("token", token, "user", user)))
                    .orElse(ResponseEntity.badRequest().body(Map.of("error", "User not found")));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
        }
    }

    public static class RegisterRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}