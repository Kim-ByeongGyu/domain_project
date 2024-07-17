package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.Role;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.repository.RoleRepository;
import org.example.copiedvelog.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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

    @Transactional(readOnly = false)
    public User saveUser(String username, String name, String email, String socialId, String provider, PasswordEncoder passwordEncoder){
        User user = new User();
        user.setUsername(username);
        user.setName(name);
        user.setEmail(email);
        user.setSocialId(socialId);
        user.setProvider(provider);
        user.setPassword(passwordEncoder.encode("")); // 비밀번호는 소셜 로그인 사용자의 경우 비워둡니다.
        return userRepository.save(user);
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

    public Optional<User> findByProviderAndSocialId(String provider, String socialId){
        return userRepository.findByProviderAndSocialId(provider,socialId);
    }
}
