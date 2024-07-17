package org.example.copiedvelog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "velog_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @Column(name = "social_id")
    private String socialId;

    private String provider;

    private String avatarUrl;

    public void addRole(Role role) {
        roles.add(role);
    }


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Velog> velogs = new HashSet<>();

    public void addVelog(Velog velog) {
        velogs.add(velog);
        velog.setOwner(this);
    }

    public User(String socialId, String provider, String username, String email, String avatarUrl) {
        this.socialId = socialId;
        this.provider = provider;
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

}
