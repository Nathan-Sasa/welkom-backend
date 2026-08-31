package com.nathdev.welkom.exceptions.invitation;

import java.util.UUID;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(UUID uuid) {

        super("Aucune invitation trouvé avec uuid : " + uuid);
    }
}
