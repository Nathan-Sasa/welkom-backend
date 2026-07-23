package com.nathdev.welkom.dto.user;

import com.nathdev.welkom.models.Profile;
import com.nathdev.welkom.models.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileDto {

    private String username;
    private String email;
    private String avatar;
    private String avatar_public;
    private String Role;
    private LocalDateTime lastLogin;

    public static ProfileDto fromEntity (
            Profile profile,
            User user
    ) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(profile.getUsername());
        profileDto.setEmail(profile.getEmail());
        profileDto.setAvatar(profile.getAvatar());
        profileDto.setAvatar_public(profile.getAvatar_public());
        profileDto.setLastLogin(profile.getLastLogin());
        profileDto.setRole(user.getRole());
        return profileDto;
    }
}
