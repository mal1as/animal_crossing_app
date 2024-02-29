package com.itmo.coursework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableWorkerDTO {

    private Long userId;
    private String username;
    private Long curReserveId;
    private String curReserveName;
    private Integer reserveAvailableLoad;
}
