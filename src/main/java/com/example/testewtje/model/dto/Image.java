package com.example.testewtje.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Image {

    private String name;
    private String contentType;
    private BigDecimal picSize;
    private String reference;
    private List<Tag> tags;

}
