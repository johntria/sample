package com.logicea.cards.requests;

import com.logicea.cards.models.Status;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SearchCardCriteriaDTO {
    private String name;
    private String color;
    @Enumerated
    private Status status;
    private LocalDateTime creationDate;
    private Integer page = 0;
    private Integer size = 10;
    private List<SortByDTO> sortMap;
}