package com.nathdev.welkom.models;


import com.nathdev.welkom.converter.JsonMapConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "customizedTemplates")
public class CustomizedTemplates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private UUID uuid;

    private String name;

    @Column(name = "custom_image1")
    private String customImage1;

    @Column(name = "custom_image2")
    private String customImage2;

    @Column(name = "custom_image3")
    private String customImage3;

    @Column(name = "custom_has_cadre")
    private boolean customHasCadre;

    @Column(name = "custom_cadre_url")
    private String customCadreUrl;

    @Column(name = "custom_font_title")
    private String customFontTitle;

    @Column(name = "custom_font_body")
    private String customFontBody;

    @Column(name = "custom_color_primary", length = 7)
    private String customColorPrimary;

    @Column(name = "custom_color_accent", length = 7)
    private String customColorAccent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "template_id",
            nullable = false
    )
    private Template template;

    @OneToOne
    @JoinColumn(
            name = "event_id",
            nullable = false,
            unique = true
    )
    private Event event;

//    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "content_data", columnDefinition = "LONGTEXT")
    private Map<String, Object> contentData;

//    public Boolean hasCadre() {
//    }
}
