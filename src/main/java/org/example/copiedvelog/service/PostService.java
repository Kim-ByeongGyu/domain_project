package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.Like;
import org.example.copiedvelog.entity.Post;
import org.example.copiedvelog.entity.User;
import org.example.copiedvelog.entity.Velog;
import org.example.copiedvelog.repository.LikeRepository;
import org.example.copiedvelog.repository.PostRepository;
import org.example.copiedvelog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public Post createPost(Post post) {
        post.setLikes(0);
        post.setViews(0);
        post.setCreatedDate(LocalDateTime.now());
        post.setUpdatedDate(LocalDateTime.now());
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Post> findByTitleContaining(String title) {
        return postRepository.findByTitleContaining(title);
    }

    @Transactional(readOnly = true)
    public List<Post> findPostsByUserAndVelog(User user, Velog velog) {
        return postRepository.findByUserAndVelog(user, velog);
    }

    @Transactional(readOnly = true)
    public Optional<Post> findByIdAndUserAndVelog(Long id, User user, Velog velog) {
        return postRepository.findByIdAndUserAndVelog(id, user, velog);
    }

    @Transactional(readOnly = true)
    public Post findById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void toggleLike(Long postId, String username) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            User user = userRepository.findByUsername(username);

            Optional<Like> likeOpt = likeRepository.findByPostAndUser(post, user);
            if (likeOpt.isPresent()) {
                // 이미 좋아요를 누른 상태이므로 좋아요 해제
                likeRepository.delete(likeOpt.get());
                post.setLikes(post.getLikes() - 1);
            } else {
                // 좋아요 추가
                Like like = new Like();
                like.setPost(post);
                like.setUser(user);
                likeRepository.save(like);
                post.setLikes(post.getLikes() + 1);
            }
            postRepository.save(post); // 저장
        } else {
            throw new IllegalArgumentException("Post not found with id: " + postId);
        }
    }

    public String getVelogNameByPostId(Long postId) {
        return postRepository.getVelogNameByPostId(postId).getName();
    }
}
