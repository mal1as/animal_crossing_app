package com.itmo.coursework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "climate")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Climate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "temp_min_avg")
    private int tempMinAvg;

    @Column(name = "temp_max_avg")
    private int tempMaxAvg;

    @OneToMany(mappedBy = "climate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Animal> animals;

    @OneToMany(mappedBy = "climate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Reserve> reserves;
}
