package com.nathdev.welkom.exceptions.invitation;

public class InvitationAlreadyExistsException extends RuntimeException {
    public InvitationAlreadyExistsException() {
        super("Une invitation existe deja pour cet invité. Un(e) invité(e) ne peut posseder qu'une seule invitation");
    }
}
