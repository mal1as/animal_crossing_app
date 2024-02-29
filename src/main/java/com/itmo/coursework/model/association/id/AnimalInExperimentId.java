package com.itmo.coursework.model.association.id;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class AnimalInExperimentId implements Serializable {

    @Column(name = "experiment_id")
    private Long experimentId;

    @Column(name = "animal_id")
    private Long animalId;
}
