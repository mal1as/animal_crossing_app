package com.itmo.coursework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itmo.coursework.model.association.AnimalReserve;
import com.itmo.coursework.model.association.ReserveWorker;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "reserve")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Reserve {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "climate_id", referencedColumnName = "id")
    private Climate climate;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<ReserveWorker> reserveWorkerSet;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<AnimalReserve> animalReserveSet;

    @OneToMany(mappedBy = "reserve", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    private Set<Report> reports;
}
