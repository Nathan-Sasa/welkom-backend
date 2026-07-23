package com.nathdev.welkom.dto.user;

import com.nathdev.welkom.models.User;
import lombok.Data;

@Data
public class AuthResponseDto {
    private String username;
    private String email;
    private String avatar;
//    private String avatar_public;
    private String first_name;
    private String role;

    public static AuthResponseDto fromEntity (
            User user
    ) {
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setUsername(user.getProfile().getUsername());
        authResponseDto.setEmail(user.getProfile().getEmail());
        authResponseDto.setAvatar(user.getProfile().getAvatar());
        authResponseDto.setFirst_name(user.getProfile().getFirst_name());
        authResponseDto.setRole(user.getRole());
        return authResponseDto;
    }
}
