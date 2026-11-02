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
//같은 강사의 같은 날짜·시간에는 수업이 하나만 존재하도록 DB에서 보장
@Table(
        name = "class_sessions",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_class_sessions_instructor_slot",
                columnNames = {"instructorFk", "sessionDate", "sessionTime"}
        )
)
public class ClassSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionPk;

    @Column(nullable = false)
    private Long enrollmentFk;

    @Column(nullable = false)
    private Long instructorFk;

    @Column(nullable = false)
    private LocalDate sessionDate;

    @Column(nullable = false)
    private LocalTime sessionTime;

    @Column(nullable = false)
    private Integer sessionStatus;
}