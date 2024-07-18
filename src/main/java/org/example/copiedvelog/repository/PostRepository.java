package org.example.copiedvelog.repository;

import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    List<Post> findByTitleContaining(String title);
    List<Post> findByUserAndVelog(User user, Velog velog);
    Optional<Post> findByIdAndUserAndVelog(Long id, User user, Velog velog);
    Post findById(long id);
    Velog getVelogNameByPostId(long id);
}
