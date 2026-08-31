package com.nathdev.welkom.exceptions.guest;

import java.util.UUID;


public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(UUID uuid) {
        super("Aucun invité trouvé avec cet uuid : " + uuid);
    }
}
