package com.fleet.fleet_telemetry.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fleet.fleet_telemetry.model.User;
import com.fleet.fleet_telemetry.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setFullName(request.get("fullName"));
            user.setUsername(request.get("username"));
            user.setEmail(request.get("email"));
            user.setPassword(request.get("password"));
            user.setRole("FLEET_MANAGER");

            User saved = userService.registerUser(user);

            response.put("success", true);
            response.put("message", "Account registered successfully!");
            response.put("username", saved.getUsername());
            response.put("fullName", saved.getFullName());
            response.put("role", saved.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException ex) {
            response.put("success", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", "Registration failed: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        String usernameOrEmail = credentials.get("usernameOrEmail");
        String password = credentials.get("password");

        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Username and password are required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<User> userOpt = userService.authenticate(usernameOrEmail.trim(), password.trim());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            response.put("success", true);
            response.put("message", "Login successful! Welcome back.");
            response.put("username", user.getUsername());
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid username/email or password.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String usernameOrEmail = request.get("usernameOrEmail");
        String newPassword = request.get("newPassword");

        if (usernameOrEmail == null || usernameOrEmail.trim().isEmpty()) {
            response.put("success", false);
            response.put("message", "Username or email is required.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (newPassword == null || newPassword.trim().length() < 6) {
            response.put("success", false);
            response.put("message", "New password must be at least 6 characters.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        boolean success = userService.resetPassword(usernameOrEmail.trim(), newPassword.trim());
        if (success) {
            response.put("success", true);
            response.put("message", "Password has been successfully reset! You can now sign in.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "User with given username or email was not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
