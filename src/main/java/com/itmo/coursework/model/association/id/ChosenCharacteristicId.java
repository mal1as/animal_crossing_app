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
public class ChosenCharacteristicId implements Serializable {

    @Column(name = "experiment_id")
    private Long experimentId;

    @Column(name = "characteristic_id")
    private Long characteristicId;
}
