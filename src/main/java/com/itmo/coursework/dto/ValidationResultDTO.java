package com.itmo.coursework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResultDTO {

    private boolean result;
    private String message;
}
