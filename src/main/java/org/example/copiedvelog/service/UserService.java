package org.example.copiedvelog.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
