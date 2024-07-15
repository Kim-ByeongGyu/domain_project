package org.example.copiedvelog.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "social_user")
@Data
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String socialId;
    private String provider;
    private String username;
    private String email;
    private String avatarUrl;
}
