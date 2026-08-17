package com.nathdev.welkom.controllers;

import com.nathdev.welkom.services.CustomizedTemplatesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customized")
@RequiredArgsConstructor
public class CustomizedTemplateController {

    private final CustomizedTemplatesService customizedTemplatesService;

    @GetMapping("/user")
    public ResponseEntity<?> User() {
        return ResponseEntity.ok(customizedTemplatesService.user());
    }
}
