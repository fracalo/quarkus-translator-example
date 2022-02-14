package com.fcalo.translator.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "Translation.findAll", query = "SELECT t FROM Translation t where t.category.id = :category_id", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Table(name = "translations")
public class Translation {

    @SequenceGenerator(name = "translationSeq", sequenceName = "translation_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "translationSeq", strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String en;
    private String it;
    private String fr;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id")
    private TranslationCategory category;
}
