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

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        Template template = templateRepository.findByUuid(request.templateUuid())
                .orElseThrow(() -> new TemplateNotFoundException(request.templateUuid()));

        if (customizedTemplatesRepository.findByEvent(event).isPresent()) {
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

    public CustomizeTemplateResponse getByEvent(UUID eventUuid) {
        User user = authenticateUser.getUser();

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        CustomizedTemplates customizedTemplate = customizedTemplatesRepository.findByEvent(event)
                .orElseThrow(() -> new TemplateNotFoundException(eventUuid));

        return toResponse(customizedTemplate);
    }

    public CustomizeTemplateResponse updateCustomizedTempale(
            UUID eventUuid,
            CustomizeTemplateRequest request
    ) {
        User user =authenticateUser.getUser();

        Event event = eventRepository.findByUuidAndDeletedAtIsNull(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));

        if (!event.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedCustomException("Cet événement ne vous appartient pas !");
        }

        CustomizedTemplates customizedTemplate = customizedTemplatesRepository.findByEvent(event)
                .orElseThrow(() -> new TemplateNotFoundException(eventUuid));

        if (request.name() != null) {
            customizedTemplate.setName(request.name());
        }
        if (request.customImage1() != null) {
            customizedTemplate.setCustomImage1(request.customImage1());
        }
        if (request.customImage2() != null) {
            customizedTemplate.setCustomImage2(request.customImage2());
        }
        if  (request.customImage3() != null) {
            customizedTemplate.setCustomImage3(request.customImage3());
        }
        if (request.customHasCadre()) {
            customizedTemplate.setCustomHasCadre(true);
        }
        if (request.customCadreUrl() != null) {
            customizedTemplate.setCustomCadreUrl(request.customCadreUrl());
        }
        if (request.customFontTitle() != null) {
            customizedTemplate.setCustomFontTitle(request.customFontTitle());
        }
        if (request.customFontBody() != null) {
            customizedTemplate.setCustomFontBody(request.customFontBody());
        }
        if (request.customColorPrimary() != null) {
            customizedTemplate.setCustomColorPrimary(request.customColorPrimary());
        }
        if (request.customColorAccent() != null) {
            customizedTemplate.setCustomColorAccent(request.customColorAccent());
        }

        CustomizedTemplates updated = customizedTemplatesRepository.save(customizedTemplate);

        return toResponse(updated);
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

    public User user() {
        return authenticateUser.getUser();
    }
}
