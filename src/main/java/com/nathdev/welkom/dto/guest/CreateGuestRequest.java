package com.nathdev.welkom.dto.guest;

public record CreateGuestRequest(
        String firstName,
        String lastName,
        String email,
        String telephone,
        String category
) {
}
