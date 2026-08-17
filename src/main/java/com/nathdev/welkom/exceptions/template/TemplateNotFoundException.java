package com.nathdev.welkom.exceptions.template;

import java.util.UUID;

public class TemplateNotFoundException extends  RuntimeException{
    public TemplateNotFoundException(UUID uuid){
        super("Template non trouvé avec cet uuid : " + uuid);
    }
}
