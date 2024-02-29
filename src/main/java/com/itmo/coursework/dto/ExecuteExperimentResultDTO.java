package com.itmo.coursework.dto;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecuteExperimentResultDTO {

    private Status status;
    private Animal animal;
}
