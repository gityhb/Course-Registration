package com.sugang.coursepage.repository;

import com.sugang.coursepage.entity.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
    // 특정 날짜의 모든 세션을 찾는 쿼리 메서드
    List<ClassSession> findBySessionDate(LocalDate sessionDate);
}