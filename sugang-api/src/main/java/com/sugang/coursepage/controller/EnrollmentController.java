package com.sugang.coursepage.controller;

import com.sugang.coursepage.dto.EnrollmentRequestDto;
import com.sugang.coursepage.entity.Enrollment;
import com.sugang.coursepage.exception.DuplicateBookingException;
import com.sugang.coursepage.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody EnrollmentRequestDto requestDto) {
        try {
            Enrollment newEnrollment = enrollmentService.createEnrollment(requestDto);
            return ResponseEntity.ok(newEnrollment);
        } catch (DuplicateBookingException e) {
            //409: 다른 사람이 먼저 신청한 시간
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}