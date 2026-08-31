package com.nathdev.welkom.exceptions.template;

import java.util.UUID;

public class TemplateNotFoundException extends  RuntimeException{
    public TemplateNotFoundException(UUID uuid){
        super("Aucune catalogue trouvé avec cet uuid : " + uuid);
    }
}
