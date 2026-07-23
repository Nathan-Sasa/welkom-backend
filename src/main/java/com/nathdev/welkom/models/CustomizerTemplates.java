package com.nathdev.welkom.models;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
@Table(name = "customizerTemplates")
public class CustomizerTemplates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "event_id", nullable = false, unique = true)
    private Event event;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "custom_background_url")
    private String customBackgroundUrl;

    @Column(name = "cadre_url")
    private String cadre;

    @Column(name = "has_cadre")
    private boolean hasCadre = false;

    @Column(name = "custom_font_title")
    private String customFontTitle;

    @Column(name = "custom_font_body")
    private String customFontBody;

    @Column(name = "custom_color_primary", length = 7)
    private String customColorPrimary;

    @Column(name = "custom_color_accent", length = 7)
    private String customColorAccent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "content_data")
    private Map<String, Object> contentData;

}
