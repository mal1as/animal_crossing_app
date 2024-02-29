package com.itmo.coursework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itmo.coursework.model.association.AnimalCharacteristic;
import com.itmo.coursework.model.association.CharacteristicInConflict;
import com.itmo.coursework.model.association.ChosenCharacteristic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "characteristic")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Characteristic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "coefficient")
    private Integer coefficient;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<CharacteristicInConflict> characteristicInConflictSet;

    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<AnimalCharacteristic> animalCharacteristicSet;

    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<ChosenCharacteristic> chosenCharacteristicSet;
}
