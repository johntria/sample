package com.logicea.cards.requests;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.logicea.cards.models.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class SearchCardCriteriaDTO {
    private String name;
    private String color;
    @Enumerated(EnumType.STRING)
    private Status status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime creationDate;
    private Integer page = 0;
    private Integer size = 10;
    private List<SortByDTO> sortMap;
}