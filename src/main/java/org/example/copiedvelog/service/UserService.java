package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.Role;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.repository.RoleRepository;
import org.example.copiedvelog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public User saveUser(User user) {
        // 비밀번호 암호화 등 기타 사용자 등록 로직을 여기에 추가합니다.

        // 사용자 저장
        User savedUser = userRepository.save(user);

        // 기본 권한 부여
        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole != null) {
            savedUser.addRole(userRole);
            userRepository.save(savedUser);
        } else {
            throw new RuntimeException("Default role not found");
        }
        savedUser = userRepository.save(savedUser);

        return savedUser;

    }



    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }
}
