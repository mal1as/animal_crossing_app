package com.itmo.coursework.model.association.id;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CharacteristicInConflictId implements Serializable {

    @Column(name = "first_characteristic_id")
    private Long firstCharacteristicId;

    @Column(name = "second_characteristic_id")
    private Long secondCharacteristicId;
}
