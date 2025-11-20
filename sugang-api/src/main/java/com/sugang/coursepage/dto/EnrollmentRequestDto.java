package com.sugang.coursepage.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EnrollmentRequestDto {
    private Long studentFk;
    private Long courseFk;
    private Long instructorFk;
    private LocalDate startDate;
    private int classDay;
    private String startTime; //"HH:mm" 형식
    private int duration;
    private int countSession;
}