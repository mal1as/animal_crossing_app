package com.itmo.coursework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itmo.coursework.model.association.AnimalCharacteristic;
import com.itmo.coursework.model.association.AnimalInExperiment;
import com.itmo.coursework.model.association.AnimalReserve;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "animal")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Animal {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_artificial")
    private boolean isArtificial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "climate_id", referencedColumnName = "id")
    private Climate climate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_class_id", referencedColumnName = "id")
    private AnimalClass animalClass;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<AnimalCharacteristic> animalCharacteristicSet;

    @OneToOne(mappedBy = "createdAnimal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Experiment createdExperiment;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<AnimalInExperiment> animalInExperimentSet;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<AnimalReserve> animalReserveSet;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Report> reports;
}
