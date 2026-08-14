package com.nathdev.welkom.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Data
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @UuidGenerator
    @Column(unique = true, nullable = false)
    private String uuid;

    private String name;
    private String category;

    @Column(name = "catalogue_img_url")
    private String catalogueImgUrl;

    @Column(name = "image1")
    private String image1;

    @Column(name = "image2")
    private String image2;

    @Column(name = "image3")
    private String image3;

    @Column(name = "cadre_url")
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

    @JdbcTypeCode(SqlTypes.LONGNVARCHAR)
    @Column(name = "default_config")
    private Map<String, Object> defaultConfig ;

}
