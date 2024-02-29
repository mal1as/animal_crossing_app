package com.itmo.coursework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itmo.coursework.model.association.AnimalInExperiment;
import com.itmo.coursework.model.association.ChosenCharacteristic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "experiment")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Experiment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;

    @Column(name = "start_date")
    private LocalDate startDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_animal_id", referencedColumnName = "id")
    private Animal createdAnimal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "experimenter_id", referencedColumnName = "id")
    private User experimenter;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<AnimalInExperiment> animalInExperimentSet;

    @OneToMany(mappedBy = "experiment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<ChosenCharacteristic> chosenCharacteristicSet;
}
