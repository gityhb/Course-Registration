package com.sugang.coursepage.service;

import com.sugang.coursepage.dto.EnrollmentRequestDto;
import com.sugang.coursepage.exception.DuplicateBookingException;
import com.sugang.coursepage.repository.ClassSessionRepository;
import com.sugang.coursepage.repository.EnrollmentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class EnrollmentServiceTest {

    private static final int SESSION_COUNT = 32;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ClassSessionRepository classSessionRepository;

    @AfterEach
    void cleanUp() {
        classSessionRepository.deleteAll();
        enrollmentRepository.deleteAll();
    }

    @Test
    void 신청하면_수업_32회가_생성된다() {
        enrollmentService.createEnrollment(request(101L, 301L, "10:00"));

        assertThat(enrollmentRepository.count()).isEqualTo(1);
        assertThat(classSessionRepository.count()).isEqualTo(SESSION_COUNT);
    }

    @Test
    void 같은_강사_같은_시간에_다시_신청하면_거절되고_기존_데이터는_그대로다() {
        enrollmentService.createEnrollment(request(101L, 301L, "10:00"));

        assertThatThrownBy(() -> enrollmentService.createEnrollment(request(102L, 301L, "10:00")))
                .isInstanceOf(DuplicateBookingException.class);

        //실패한 신청은 Enrollment까지 함께 롤백되어야 함
        assertThat(enrollmentRepository.count()).isEqualTo(1);
        assertThat(classSessionRepository.count()).isEqualTo(SESSION_COUNT);
    }

    @Test
    void 강사나_시간이_다르면_둘_다_신청된다() {
        enrollmentService.createEnrollment(request(101L, 301L, "10:00"));
        enrollmentService.createEnrollment(request(102L, 301L, "10:30"));
        enrollmentService.createEnrollment(request(103L, 302L, "10:00"));

        assertThat(enrollmentRepository.count()).isEqualTo(3);
        assertThat(classSessionRepository.count()).isEqualTo(SESSION_COUNT * 3);
    }

    @Test
    void 여러_명이_같은_시간에_동시에_신청하면_한_명만_성공한다() throws Exception {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<Boolean>> results = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            long studentId = 101L + i;
            results.add(executor.submit(() -> {
                ready.countDown();
                start.await();
                try {
                    enrollmentService.createEnrollment(request(studentId, 301L, "10:00"));
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }));
        }

        //모든 스레드가 준비되면 한꺼번에 출발
        ready.await();
        start.countDown();

        int successCount = 0;
        for (Future<Boolean> result : results) {
            if (result.get(30, TimeUnit.SECONDS)) {
                successCount++;
            }
        }
        executor.shutdown();

        assertThat(successCount).isEqualTo(1);
        assertThat(enrollmentRepository.count()).isEqualTo(1);
        assertThat(classSessionRepository.count()).isEqualTo(SESSION_COUNT);
    }

    private EnrollmentRequestDto request(Long studentId, Long instructorId, String startTime) {
        EnrollmentRequestDto dto = new EnrollmentRequestDto();
        dto.setStudentFk(studentId);
        dto.setCourseFk(201L);
        dto.setInstructorFk(instructorId);
        dto.setStartDate(LocalDate.of(2025, 12, 1));
        dto.setClassDay(1);
        dto.setStartTime(startTime);
        dto.setDuration(20);
        dto.setCountSession(SESSION_COUNT);
        return dto;
    }
}
