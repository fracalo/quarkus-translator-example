package com.fcalo.translator.Dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class TranslationInDto {
    private String en;
    private String it;
    private String fr;
    private Long categoryId;
}
