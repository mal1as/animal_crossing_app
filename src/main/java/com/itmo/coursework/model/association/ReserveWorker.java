package com.itmo.coursework.model.association;

import com.itmo.coursework.model.Reserve;
import com.itmo.coursework.model.Role;
import com.itmo.coursework.model.User;
import com.itmo.coursework.model.association.id.ReserveWorkerId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "reserve_worker")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReserveWorker {

    @EmbeddedId
    private ReserveWorkerId reserveWorkerId = new ReserveWorkerId();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("reserveId")
    @JoinColumn(name = "reserve_id", referencedColumnName = "id")
    private Reserve reserve;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("workerId")
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private User worker;

    @Column(name = "available_load")
    private Integer availableLoad;
}
