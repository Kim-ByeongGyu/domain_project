package org.example.copiedvelog.repository;

import org.example.copiedvelog.entity.Like;
import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
}
