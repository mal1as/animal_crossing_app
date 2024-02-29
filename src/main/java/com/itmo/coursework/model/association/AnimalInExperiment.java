package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Experiment;
import com.itmo.coursework.model.association.id.AnimalInExperimentId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "animal_in_experiment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnimalInExperiment {

    @EmbeddedId
    private AnimalInExperimentId animalInExperimentId = new AnimalInExperimentId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("experimentId")
    @JoinColumn(name = "experiment_id", referencedColumnName = "id")
    private Experiment experiment;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("animalId")
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;
}
