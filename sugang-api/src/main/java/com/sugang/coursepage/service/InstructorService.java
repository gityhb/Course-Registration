package com.sugang.coursepage.service;

import com.sugang.coursepage.dto.AvailableTimeDto;
import com.sugang.coursepage.entity.InstructorAvailability;
import com.sugang.coursepage.repository.InstructorAvailabilityRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorAvailabilityRepository availabilityRepository;

    public List<AvailableTimeDto> getAvailableTimes(String instructorId) {

        List<InstructorAvailability> availabilities = availabilityRepository.findByInstructorId(instructorId);

        return availabilities.stream()
                .map(AvailableTimeDto::new)
                .collect(Collectors.toList());
    }
}