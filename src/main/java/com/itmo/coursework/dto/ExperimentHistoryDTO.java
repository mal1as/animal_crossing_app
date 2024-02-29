package com.itmo.coursework.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExperimentHistoryDTO {

    private Long experimentId;
    private Long statusId;
    private LocalDate experimentDate;
    private Long createdAnimalId;
    private Long experimenterId;
}
