package com.nathdev.welkom.models;

import com.nathdev.welkom.converter.JsonMapConverter;
import jakarta.persistence.*;
//import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private UUID uuid;

    private String name;
    private String category;

    @Column(name = "catalogue_img_url")
    private String catalogueImgUrl;

    private String image1;
    private String image2;
    private String image3;

    private String cadre;

    @Column(name = "has_cadre")
    private boolean hasCadre;

    @Column(name = "font_title")
    private String fontTitle;

    @Column(name = "font_body")
    private String fontBody;

    @Column(name = "color_primary", length = 7)
    private String colorPrimary;

    @Column(name = "color_accent", length = 7)
    private String colorAccent;

    @OneToMany(mappedBy = "template")
    private List<CustomizedTemplates> customizedTemplates;

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "default_config", columnDefinition = "LONGTEXT")
    private Map<String, Object> defaultConfig ;
}
