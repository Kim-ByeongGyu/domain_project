package org.example.copiedvelog.repository;

import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    public List<Post> findByUser(User user);
    public List<Post> findByTitleContaining(String title);
}
