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
public class ReserveWorkerId implements Serializable {

    @Column(name = "reserve_id")
    private Long reserveId;

    @Column(name = "worker_id")
    private Long workerId;
}
