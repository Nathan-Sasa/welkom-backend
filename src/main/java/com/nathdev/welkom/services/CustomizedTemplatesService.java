package com.nathdev.welkom.services;

import com.nathdev.welkom.components.AuthenticateUser;
import com.nathdev.welkom.dto.customizedTemplate.CustomizeTemplateRequest;
import com.nathdev.welkom.dto.customizedTemplate.CustomizeTemplateResponse;
import com.nathdev.welkom.exceptions.accessDenied.AccessDeniedCustomException;
import com.nathdev.welkom.exceptions.customizedTemplate.CustomizedTemplateAlreadyExistsException;
import com.nathdev.welkom.exceptions.event.EventNotFoundException;
import com.nathdev.welkom.exceptions.template.TemplateNotFoundException;
import com.nathdev.welkom.models.CustomizedTemplates;
import com.nathdev.welkom.models.Event;
import com.nathdev.welkom.models.Template;
import com.nathdev.welkom.models.User;
import com.nathdev.welkom.repositories.CustomizedTemplatesRepository;
import com.nathdev.welkom.repositories.EventRepository;
import com.nathdev.welkom.repositories.TemplateRepository;
import com.nathdev.welkom.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomizedTemplatesService {

    private final EventRepository eventRepository;
    private final TemplateRepository templateRepository;
    private final CustomizedTemplatesRepository customizedTemplatesRepository;
    private final AuthenticateUser authenticateUser;
    private final UserRepository userRepository;

    public CustomizeTemplateResponse create(
            UUID eventUuid,
            CustomizeTemplateRequest request
    ) {
        User user = authenticateUser.getUser();

        Event event = eventRepository.findByUuid(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        Template template = templateRepository.findByUuid(request.templateUuid())
                .orElseThrow(() -> new TemplateNotFoundException(request.templateUuid()));

        if (event.getCustomizedTemplate() != null) {
            throw new CustomizedTemplateAlreadyExistsException(eventUuid);
        }

        CustomizedTemplates customizedTemplate = new CustomizedTemplates();

        customizedTemplate.setEvent(event);
        customizedTemplate.setTemplate(template);

        customizedTemplate.setName(request.name());
        customizedTemplate.setCustomImage1(request.customImage1());
        customizedTemplate.setCustomImage2(request.customImage2());
        customizedTemplate.setCustomImage3(request.customImage3());
        customizedTemplate.setCustomHasCadre(request.customHasCadre());
        customizedTemplate.setCustomCadreUrl(request.customCadreUrl());
        customizedTemplate.setCustomFontTitle(request.customFontTitle());
        customizedTemplate.setCustomFontBody(request.customFontBody());
        customizedTemplate.setCustomColorPrimary(request.customColorPrimary());
        customizedTemplate.setCustomColorAccent(request.customColorAccent());
        customizedTemplate.setContentData(request.contentData());

        CustomizedTemplates saved =  customizedTemplatesRepository.save(customizedTemplate);

        return toResponse(saved);
    }

    private CustomizeTemplateResponse toResponse(CustomizedTemplates customizedTemplate) {
        return new CustomizeTemplateResponse(
                customizedTemplate.getUuid(),
                customizedTemplate.getTemplate().getUuid(),
                customizedTemplate.getName(),
                customizedTemplate.getCustomImage1(),
                customizedTemplate.getCustomImage2(),
                customizedTemplate.getCustomImage3(),
                customizedTemplate.isCustomHasCadre(),
                customizedTemplate.getCustomCadreUrl(),
                customizedTemplate.getCustomFontTitle(),
                customizedTemplate.getCustomFontBody(),
                customizedTemplate.getCustomColorPrimary(),
                customizedTemplate.getCustomColorAccent(),
                customizedTemplate.getContentData()
        );
    }

//    public CustomizedTemplates createCustomizedTemplates(UUID templateUuid, String event_id) {
//
//        Event existEvent = eventRepository.findBySecureId(event_id)
//                .orElseThrow(() -> new RuntimeException("Évenement introuvable"));
//
//        Template existTemplate = templateRepository.findByUuid(templateUuid)
//                .orElseThrow(() -> new RuntimeException("Template introuvable"));
//
//        CustomizedTemplates customizedTemplates = new CustomizedTemplates();
//        customizedTemplates.setEvent(existEvent);
//        customizedTemplates.setName(existTemplate.getName());
//        customizedTemplates.setCustomImage1(existTemplate.getImage1());
//        customizedTemplates.setCustomImage2(existTemplate.getImage2());
//        customizedTemplates.setCustomImage3(existTemplate.getImage3());
//        customizedTemplates.setCustomHasCadre(existTemplate.isHasCadre());
//        customizedTemplates.setCustomCadreUrl(existTemplate.getCadre());
//        customizedTemplates.setCustomFontTitle(existTemplate.getFontTitle());
//        customizedTemplates.setCustomFontBody(existTemplate.getFontBody());
//        customizedTemplates.setCustomColorPrimary(existTemplate.getColorPrimary());
//        customizedTemplates.setCustomColorAccent(existTemplate.getColorAccent());
//        customizedTemplates.setContentData(existTemplate.getDefaultConfig());
//
//        return customizedTemplatesRepository.save(customizedTemplates);
//    }

    public User user() {
        return authenticateUser.getUser();
    }
}
