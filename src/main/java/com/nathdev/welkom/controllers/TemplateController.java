package com.nathdev.welkom.controllers;

import com.nathdev.welkom.models.Template;
import com.nathdev.welkom.security.JwtUtils;
import com.nathdev.welkom.services.TemplateService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {
    private final TemplateService templateService;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @GetMapping("/list")
    public ResponseEntity<@NotNull List<Map<Object,Object>>> getAllTemplates() {
        return templateService.getTemplates();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<@NotNull Template> getTemplateById(@PathVariable UUID uuid) {
        if (uuid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return templateService.getTemplateById(uuid);
    }

    @PostMapping("/create")
    public ResponseEntity<@NotNull Template> createInvitation(@RequestBody Template template) {

        if (template.getName() != null) {
            templateService.createTemplate(template);
            return new ResponseEntity<>(new Template(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Template(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/update/{uuid}")
    public ResponseEntity<@NotNull Template> updateTemplate(
            HttpServletRequest cookie,
            @PathVariable Long id,
            @RequestBody Map<String, Object> template
    ) {
        String refreshToken = jwtUtils.getJwtFromCookies(cookie, "welkom_access");

        if (refreshToken != null && jwtUtils.isTokenExpire(refreshToken)) {
            String username = jwtUtils.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (
                    userDetails.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "WLK_ADMIN"))
                    || userDetails.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "WLK_SUPER_ADMIN"))
            ) {
                Template update = templateService.patchTemplate(id, template);
                return new ResponseEntity<>(update, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Template(), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(new Template(), HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<@NotNull Template> deleteTemplate(
            HttpServletRequest cookie,
            @PathVariable Long id
    ) {
        String refreshToken = jwtUtils.getJwtFromCookies(cookie, "welkom_access");
        if (refreshToken != null && jwtUtils.isTokenExpire(refreshToken)) {
            String username = jwtUtils.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (
                    userDetails.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "WLK_ADMIN"))
                            || userDetails.getAuthorities().stream().anyMatch(a -> Objects.equals(a.getAuthority(), "WLK_SUPER_ADMIN"))
            ) {
                Template update = templateService.deleteTemplate(id);
                return new ResponseEntity<>(update, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Template(), HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>(new Template(), HttpStatus.BAD_REQUEST);
        }
    }

}
