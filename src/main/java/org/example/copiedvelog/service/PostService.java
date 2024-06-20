package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void save(Post post) {
        postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<Post> findByUser(User user) {
        return postRepository.findByUser(user);
    }

    public List<Post> findByTitleContaining(String title) {
        return postRepository.findByTitleContaining(title);
    }
}
