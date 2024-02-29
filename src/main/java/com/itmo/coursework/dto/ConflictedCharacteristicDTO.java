package com.itmo.coursework.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConflictedCharacteristicDTO {

    private Long firstCharId;
    private Long secondCharId;
}
