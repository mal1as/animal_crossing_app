package com.itmo.coursework.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportHistoryParamsDTO {

    private Long animalId;
    private Long reserveId;
    private Integer minRate;
    private Integer maxRate;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long reporterId;
}
