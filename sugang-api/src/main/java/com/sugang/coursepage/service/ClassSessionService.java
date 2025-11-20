package com.sugang.coursepage.service;

import com.sugang.coursepage.entity.ClassSession;
import com.sugang.coursepage.repository.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassSessionService {

    private final ClassSessionRepository classSessionRepository;
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public List<String> getBookedTimes(LocalDate date, Long instructorId) {
        // [수정] 리포지토리 호출 시 강사 ID도 함께 전달
        List<ClassSession> sessions = classSessionRepository.findBySessionDateAndInstructorFk(date, instructorId);
        
        return sessions.stream()
                .map(session -> session.getSessionTime().format(TIME_FORMATTER))
                .distinct()
                .collect(Collectors.toList());
    }
}