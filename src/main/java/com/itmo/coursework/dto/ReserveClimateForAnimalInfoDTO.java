package com.itmo.coursework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveClimateForAnimalInfoDTO {

    private Long reserveId;
    private String reserveName;
    private String reserveClimateName;
    private String animalClimateName;
    private Integer tempMinDiff;
    private Integer tempMaxDiff;
    private Integer totalTempDiff;
}
