package org.example.copiedvelog.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;

    private Long userId;
    private String username;
    private String name;
}