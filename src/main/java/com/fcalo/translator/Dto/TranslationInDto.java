package com.fcalo.translator.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationInDto {
    private String en;
    private String it;
    private String fr;
    private Long categoryId;
}
