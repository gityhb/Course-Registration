package com.sugang.coursepage.controller;

import com.sugang.coursepage.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    // 특정 날짜에 이미 예약된 시간 조회 API
    @GetMapping("/booked-times")
    public ResponseEntity<List<String>> getBookedTimes(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<String> bookedTimes = classSessionService.getBookedTimes(date);
        return ResponseEntity.ok(bookedTimes);
    }
}