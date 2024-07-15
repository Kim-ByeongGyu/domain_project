package org.example.copiedvelog.repository;

import org.example.copiedvelog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User findByUsername(String username);

    Optional<User> findByProviderAndSocialId(String provider, String socialId);
}
