package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Characteristic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "animal_characteristic")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnimalCharacteristic {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Animal animal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "characteristic_id", referencedColumnName = "id")
    private Characteristic characteristic;
}
