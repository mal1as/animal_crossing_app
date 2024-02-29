package com.itmo.coursework.dto;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Reserve;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableToReportAnimalsDTO {

    private Reserve reserve;
    private List<Animal> animals;
}
