package com.sugang.coursepage.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;
import com.sugang.coursepage.service.InstructorService;
import com.sugang.coursepage.dto.AvailableTimeDto;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    // 강사 ID로 강의 가능한 시간표 조회하는 API
    @GetMapping("/{instructorId}/available-times")
    public ResponseEntity<List<AvailableTimeDto>> getInstructorAvailableTimes(
            @PathVariable String instructorId
    ) {
        List<AvailableTimeDto> times = instructorService.getAvailableTimes(instructorId);
        
        return ResponseEntity.ok(times);
    }
}