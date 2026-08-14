package com.nathdev.welkom.services;

import com.nathdev.welkom.models.CustomizedTemplates;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Template;
import com.nathdev.welkom.repositories.CustomizedTemplatesRepository;
import com.nathdev.welkom.repositories.EventRepository;
import com.nathdev.welkom.repositories.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class customizedTemplatesService {

    private final EventRepository eventRepository;
    private final TemplateRepository templateRepository;
    private final CustomizedTemplatesRepository customizedTemplatesRepository;

    public CustomizedTemplates createCustomizedTemplates(UUID templateUuid, String event_id) {

        Event existEvent = eventRepository.findBySecureId(event_id)
                .orElseThrow(() -> new RuntimeException("Évenement introuvable"));

        Template existTemplate = templateRepository.findByUuid(templateUuid)
                .orElseThrow(() -> new RuntimeException("Template introuvable"));

        CustomizedTemplates customizedTemplates = new CustomizedTemplates();
        customizedTemplates.setEvent(existEvent);
        customizedTemplates.setTemplateUuid(existTemplate.getUuid());
        customizedTemplates.setName(existTemplate.getName());
        customizedTemplates.setCategory(existTemplate.getCategory());
        customizedTemplates.setCustomImage1(existTemplate.getImage1());
        customizedTemplates.setCustomImage2(existTemplate.getImage2());
        customizedTemplates.setCustomImage3(existTemplate.getImage3());
        customizedTemplates.setCustomHasCadre(existTemplate.isHasCadre());
        customizedTemplates.setCustomCadreUrl(existTemplate.getCadre());
        customizedTemplates.setCustomFontTitle(existTemplate.getFontTitle());
        customizedTemplates.setCustomFontBody(existTemplate.getFontBody());
        customizedTemplates.setCustomColorPrimary(existTemplate.getColorPrimary());
        customizedTemplates.setCustomColorAccent(existTemplate.getColorAccent());
        customizedTemplates.setContentData(existTemplate.getDefaultConfig());

        return customizedTemplatesRepository.save(customizedTemplates);
    }
}
