package com.sugang.coursepage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentPk;

    @Column(nullable = false)
    private Long studentFk;

    @Column(nullable = false)
    private Long courseFk;

    @Column(nullable = false)
    private Long instructorFk;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer countSession;

    @Column(nullable = false)
    private Integer classDay;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Integer status;
}