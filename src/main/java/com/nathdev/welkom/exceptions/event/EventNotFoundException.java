package com.nathdev.welkom.exceptions.event;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(UUID uuid) {
        super("Aucun événement trouve avec cet uuid : " + uuid);
    }
}
