package com.itmo.coursework.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExperimentHistoryParamsDTO {

    private Long statusId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> animalIds;
    private Long experimenterId;
}
