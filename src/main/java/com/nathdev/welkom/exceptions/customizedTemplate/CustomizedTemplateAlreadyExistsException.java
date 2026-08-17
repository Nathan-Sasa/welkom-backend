package com.nathdev.welkom.exceptions.customizedTemplate;

import java.util.UUID;

public class CustomizedTemplateAlreadyExistsException extends  RuntimeException {
    public CustomizedTemplateAlreadyExistsException(UUID uuid) {
        super("Cet événement possède déjà une catalogue. Un événement peut posseder qu'une seule catalogue");
    }
}
