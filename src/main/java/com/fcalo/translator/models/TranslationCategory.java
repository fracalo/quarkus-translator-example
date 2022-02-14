package com.fcalo.translator.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NamedQuery(name = "TranslationCategory.findAll", query = "SELECT f FROM TranslationCategory f ", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Table(name = "translation_categories")
@Cacheable
public class TranslationCategory {
    @SequenceGenerator(name = "translationCatSeq", sequenceName = "translation_cat_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "translationCatSeq", strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(unique = true)
    private String name;
}
