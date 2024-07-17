package org.example.copiedvelog.service;

import lombok.RequiredArgsConstructor;
import org.example.copiedvelog.entity.SocialUser;
import org.example.copiedvelog.repository.SocialUserRepository;
import org.example.copiedvelog.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SocialUserService {
    private final SocialUserRepository socialUserRepository;

    @Transactional
    public void saveOrUpdateUser(String socialId, String provider, String username, String email, String avatarUrl) {
        SocialUser user = socialUserRepository.findBySocialIdAndProvider(socialId, provider)
                .orElse(new SocialUser());

        user.setSocialId(socialId);
        user.setProvider(provider);
        user.setUsername(username);
        user.setEmail(email);
        user.setAvatarUrl(avatarUrl);

        socialUserRepository.save(user);
    }
}
