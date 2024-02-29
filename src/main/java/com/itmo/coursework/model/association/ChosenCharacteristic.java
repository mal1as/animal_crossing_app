package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Characteristic;
import com.itmo.coursework.model.Experiment;
import com.itmo.coursework.model.association.id.ChosenCharacteristicId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "chosen_characteristic")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChosenCharacteristic {

    @EmbeddedId
    private ChosenCharacteristicId chosenCharacteristicId = new ChosenCharacteristicId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("experimentId")
    @JoinColumn(name = "experiment_id", referencedColumnName = "id")
    private Experiment experiment;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("characteristicId")
    @JoinColumn(name = "characteristic_id", referencedColumnName = "id")
    private Characteristic characteristic;
}
