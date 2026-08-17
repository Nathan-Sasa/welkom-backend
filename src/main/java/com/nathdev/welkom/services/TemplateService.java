package com.nathdev.welkom.services;

import com.nathdev.welkom.dto.template.CreateTemplateRequest;
import com.nathdev.welkom.dto.template.TemplateResponse;
import com.nathdev.welkom.dto.template.UpdateTemplateRequest;
import com.nathdev.welkom.exceptions.template.TemplateNotFoundException;
import com.nathdev.welkom.models.Template;
import com.nathdev.welkom.repositories.TemplateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public List<TemplateResponse> getTemplates(){
        return templateRepository.findAll()
                .stream()
                .map(template -> new TemplateResponse(
                        template.getUuid(),
                        template.getName(),
                        template.getCategory(),
                        template.getCatalogueImgUrl(),
                        template.getColorPrimary(),
                        template.getColorAccent(),
                        template.getFontTitle(),
                        template.getFontBody(),
                        template.getImage1(),
                        template.getImage2(),
                        template.getImage3(),
                        template.isHasCadre(),
                        template.getDefaultConfig()
                ))
                .toList();
    }

    public TemplateResponse getTemplateById(UUID uuid){
        Optional <Template> template = templateRepository.findByUuid(uuid);
        return template.map(this::toResponse).orElse(null);
    }

    public TemplateResponse createTemplate(CreateTemplateRequest request) {
        Template template = new Template();

        template.setName(request.name());
        template.setCategory(request.category());
        template.setCatalogueImgUrl(request.catalogueImgUrl());
        template.setColorPrimary(request.colorPrimary());
        template.setColorAccent(request.colorAccent());
        template.setFontTitle(request.fontTitle());
        template.setFontBody(request.fontBody());
        template.setImage1(request.image1());
        template.setImage2(request.image2());
        template.setImage3(request.image3());
        template.setHasCadre(request.hasCadre());
        template.setDefaultConfig(request.defaultConfig());

        Template savedTemplate = templateRepository.save(template);
        return toResponse(savedTemplate);

    }

    public TemplateResponse updateTemplate(UUID uuid, UpdateTemplateRequest request) {
        Template template = templateRepository.findByUuid(uuid)
                .orElseThrow(() -> new TemplateNotFoundException(uuid));

        if (request.name() != null) {
            template.setName(request.name());
        }
        if (request.category() != null) {
            template.setCategory(request.category());
        }
        if (request.catalogueImgUrl() != null) {
            template.setCatalogueImgUrl(request.catalogueImgUrl());
        }
        if (request.colorPrimary() != null) {
            template.setColorPrimary(request.colorPrimary());
        }
        if (request.colorAccent() != null) {
            template.setColorAccent(request.colorAccent());
        }
        if (request.fontTitle() != null) {
            template.setFontTitle(request.fontTitle());
        }
        if (request.fontBody() != null) {
            template.setFontBody(request.fontBody());
        }
        if (request.image1() != null) {
            template.setImage1(request.image1());
        }
        if (request.image2() != null) {
            template.setImage2(request.image2());
        }
        if (request.image3() != null) {
            template.setImage3(request.image3());
        }
        if (request.hasCadre() != null) {
            template.setHasCadre(request.hasCadre());
        }
        if (request.defaultConfig() != null) {
            template.setDefaultConfig(request.defaultConfig());
        }

        Template updateTemplate = templateRepository.save(template);
        return toResponse(updateTemplate);
    }


    public Template patchTemplate(Long id, Map<String, Object> patch) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("template introuvable"));

        patch.forEach((key, value) -> {
            if (key.equals("name")) {
                Field field = ReflectionUtils.findField(template.getClass(), key);

                if (field != null) {
                    field.setAccessible(true);
                    if (field.getType().equals(LocalDate.class)) {
                        if (value != null) {
                            LocalDate localDate = LocalDate.parse((String) value);
                            ReflectionUtils.setField(field, template, localDate);
                        }
                    }else {
                        ReflectionUtils.setField(field, template, value);
                    }
                }
            }
        });
        return templateRepository.save(template);
    }

    public void deleteTemplate(UUID uuid) {
        Template template = templateRepository.findByUuid(uuid)
                .orElseThrow(() -> new TemplateNotFoundException(uuid));

        templateRepository.delete(template);
    }


    private TemplateResponse toResponse(Template template) {
        return new TemplateResponse(
                template.getUuid(),
                template.getName(),
                template.getCategory(),
                template.getCatalogueImgUrl(),
                template.getColorPrimary(),
                template.getColorAccent(),
                template.getFontTitle(),
                template.getFontBody(),
                template.getImage1(),
                template.getImage2(),
                template.getImage3(),
                template.isHasCadre(),
                template.getDefaultConfig()
        );
    }
}
