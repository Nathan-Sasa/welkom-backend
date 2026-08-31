package com.nathdev.welkom.exceptions.customizedTemplate;

import java.util.UUID;

public class CustomizedTemplateNotFoundException extends RuntimeException {
    public CustomizedTemplateNotFoundException(
            UUID uuid
    ) {
        super("Aucune catalogue trouvée avec cet événement : " + uuid);
    }
}
