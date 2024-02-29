package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Characteristic;
import com.itmo.coursework.model.association.id.CharacteristicInConflictId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "characteristic_in_conflict")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CharacteristicInConflict {

    @EmbeddedId
    private CharacteristicInConflictId characteristicInConflictId = new CharacteristicInConflictId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("firstCharacteristicId")
    @JoinColumn(name = "first_characteristic_id", referencedColumnName = "id")
    private Characteristic firstCharacteristic;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("secondCharacteristicId")
    @JoinColumn(name = "second_characteristic_id", referencedColumnName = "id")
    private Characteristic secondCharacteristic;
}
