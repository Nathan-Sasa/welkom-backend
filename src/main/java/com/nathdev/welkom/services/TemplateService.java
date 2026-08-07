package com.nathdev.welkom.services;

import com.nathdev.welkom.models.Template;
import com.nathdev.welkom.repositories.TemplateRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public ResponseEntity<@NotNull List<Template>> getTemplates(){
        return new ResponseEntity<>(templateRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<@NotNull Template> getTemplateById(long id){
        Optional <Template> template = templateRepository.findById(id);

        return template.map(value -> new ResponseEntity<>(
                value, HttpStatus.OK
        )).orElseGet(() -> new ResponseEntity<>(
                HttpStatus.NOT_FOUND
        ));
    }

    public void createTemplate(Template template) {
        if (template.getCategory() == null || template.getCategory().isEmpty()) {
            template.setCategory(template.getCategory());
        }
        if (template.getName() == null || template.getName().isEmpty()) {
            template.setName(template.getName());
        }
        if (template.getCatalogueImgUrl() == null || template.getCatalogueImgUrl().isEmpty()) {
            template.setCatalogueImgUrl(template.getCatalogueImgUrl());
        }
        if (template.getImage1() == null || template.getImage1().isEmpty()) {
            template.setImage1(template.getImage1());
        }
        if (template.getImage2() == null || template.getImage2().isEmpty()) {
            template.setImage2(template.getImage2());
        }
        if (template.getImage3() == null || template.getImage3().isEmpty()) {
            template.setImage3(template.getImage3());
        }
        if (template.getDefaultConfig() == null || template.getDefaultConfig().isEmpty()) {
            template.setDefaultConfig(template.getDefaultConfig());
        }

        templateRepository.save(template);
    }

    public Template updateTemplate(Long id, Template template) {
        Optional <Template> existedTemplate = templateRepository.findById(id);

        if (existedTemplate.isPresent()) {
            Template update = existedTemplate.get();

            update.setName(template.getName());
            update.setCategory(template.getCategory());
            update.setDefaultConfig(template.getDefaultConfig());

            return templateRepository.save(template);
        }

        return null;
    }


    public Template patchTemplate(Long id, Map<String, Object> patch) {
//        Optional <Template> template = Optional.of(templateRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Template introuvable")));
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

    public Template deleteTemplate(Long id) {
        Optional<Template> template = templateRepository.findById(id);
        if (template.isPresent()) {
            templateRepository.deleteById(id);
        }
        return template.orElse(null);
    }
}
