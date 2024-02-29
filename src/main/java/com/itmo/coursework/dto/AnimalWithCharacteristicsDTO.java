package com.itmo.coursework.dto;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Characteristic;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalWithCharacteristicsDTO {

    private Animal animal;
    private List<Characteristic> characteristics;
}
