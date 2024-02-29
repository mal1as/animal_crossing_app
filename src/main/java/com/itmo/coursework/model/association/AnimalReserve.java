package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Reserve;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "animal_reserve")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnimalReserve {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reserve_id", referencedColumnName = "id")
    private Reserve reserve;
}
