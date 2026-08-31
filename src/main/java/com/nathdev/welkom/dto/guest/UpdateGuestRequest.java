package com.nathdev.welkom.dto.guest;

public record UpdateGuestRequest(
        String firstName,
        String lastName,
        String email,
        String telephone,
        String category
) {
}
