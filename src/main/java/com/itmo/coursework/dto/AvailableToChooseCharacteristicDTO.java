package com.itmo.coursework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableToChooseCharacteristicDTO {

    private Long characteristicId;
    private String characteristicName;
}