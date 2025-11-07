package com.sugang.coursepage.dto;

import com.sugang.coursepage.entity.InstructorAvailability;
import lombok.Getter;
import java.time.LocalTime;

@Getter
public class AvailableTimeDto {
    private int dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public AvailableTimeDto(InstructorAvailability entity) {
        this.dayOfWeek = entity.getDayOfWeek();
        this.startTime = entity.getStartTime();
        this.endTime = entity.getEndTime();
    }
}