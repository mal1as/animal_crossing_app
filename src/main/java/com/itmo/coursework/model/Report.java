package com.itmo.coursework.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "report")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Report {

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

    @Column(name = "health_rate")
    private Integer healthRate;

    @Column(name = "comment")
    private String comment;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reporter_id", referencedColumnName = "id")
    private User reporter;
}
