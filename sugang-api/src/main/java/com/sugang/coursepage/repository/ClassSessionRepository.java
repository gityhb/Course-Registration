package com.sugang.coursepage.repository;

import com.sugang.coursepage.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    // [수정] 날짜(date) AND 강사(instructorFk)가 모두 일치하는 세션만 조회
    List<ClassSession> findBySessionDateAndInstructorFk(LocalDate sessionDate, Long instructorFk);
}