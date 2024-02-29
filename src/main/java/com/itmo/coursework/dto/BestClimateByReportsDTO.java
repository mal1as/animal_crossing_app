package com.itmo.coursework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BestClimateByReportsDTO {

    private Long climateId;
    private String climateName;
    private Long reportsNum;
    private Double avgHealthRate;
}
