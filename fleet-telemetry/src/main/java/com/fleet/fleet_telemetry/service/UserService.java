package com.fleet.fleet_telemetry.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fleet.fleet_telemetry.model.User;
import com.fleet.fleet_telemetry.repo.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("FLEET_OPERATOR");
        }

        return userRepository.save(user);
    }

    public Optional<User> authenticate(String usernameOrEmail, String password) {
        if (usernameOrEmail == null || password == null) {
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public boolean resetPassword(String usernameOrEmail, String newPassword) {
        if (usernameOrEmail == null || newPassword == null || newPassword.trim().length() < 6) {
            return false;
        }
        Optional<User> userOpt = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public long count() {
        return userRepository.count();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
