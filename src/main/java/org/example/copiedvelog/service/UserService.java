package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.Role;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.repository.RoleRepository;
import org.example.copiedvelog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

//    @Transactional
//    public User saveUser(User user) {
//        // 비밀번호 암호화 등 기타 사용자 등록 로직을 여기에 추가합니다.
//
//        // 사용자 저장
//        User savedUser = userRepository.save(user);
//
//        // 기본 권한 부여
//        Role userRole = roleRepository.findByName("ROLE_USER");
//        if (userRole != null) {
//            savedUser.addRole(userRole);
//            userRepository.save(savedUser);
//        } else {
//            throw new RuntimeException("Default role not found");
//        }
//
//        // password Encoding
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        savedUser = userRepository.save(savedUser);
//
//        return savedUser;
//    }
    @Transactional
    public User registUser(User user) {
        // role 추가
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));

        // password Encoding
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }
}
