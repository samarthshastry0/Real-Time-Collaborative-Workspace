package com.samarthshastry.workspace.backend.service;

import com.samarthshastry.workspace.backend.model.User;
import com.samarthshastry.workspace.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepo.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User updateEmail(UUID id, String newEmail) {
        if(userRepo.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already exists");
        }
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEmail(newEmail);
        return userRepo.save(user);
    }

    public User updatePassword(UUID id, String currPasswordHash, String newPasswordHash) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPasswordHash().equals(currPasswordHash)) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPasswordHash(newPasswordHash);
        return userRepo.save(user);
    }

    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepo.save(user);
    }

    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }
}