package com.sugang.coursepage.service;

import com.sugang.coursepage.dto.EnrollmentRequestDto;
import com.sugang.coursepage.entity.ClassSession;
import com.sugang.coursepage.entity.Enrollment;
import com.sugang.coursepage.repository.ClassSessionRepository;
import com.sugang.coursepage.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ClassSessionRepository classSessionRepository;

    @Transactional
    public Enrollment createEnrollment(EnrollmentRequestDto dto) {
        
        //32회 수업 날짜 리스트 생성
        List<LocalDate> sessionDates = calculateSessionDates(dto.getStartDate(), dto.getClassDay(), dto.getCountSession());

        //Enrollment 엔티티 생성 및 저장
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentFk(dto.getStudentFk());
        enrollment.setCourseFk(dto.getCourseFk());
        enrollment.setInstructorFk(dto.getInstructorFk());
        enrollment.setStartDate(dto.getStartDate());
        enrollment.setEndDate(sessionDates.get(sessionDates.size() - 1));
        enrollment.setCountSession(dto.getCountSession());
        enrollment.setClassDay(dto.getClassDay());
        enrollment.setStartTime(LocalTime.parse(dto.getStartTime()));
        enrollment.setDuration(dto.getDuration());
        enrollment.setStatus(1); //1: 성공

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        Long newEnrollmentPk = savedEnrollment.getEnrollmentPk();
        
        //32개의 ClassSession 엔티티 생성 및 저장
        List<ClassSession> sessionsToSave = new ArrayList<>();
        for (LocalDate date : sessionDates) {
            ClassSession session = new ClassSession();
            session.setEnrollmentFk(newEnrollmentPk);
            session.setInstructorFk(dto.getInstructorFk());
            session.setSessionDate(date);
            session.setSessionTime(savedEnrollment.getStartTime());
            session.setSessionStatus(1); //1: 수업예정
            sessionsToSave.add(session);
        }

        classSessionRepository.saveAll(sessionsToSave);

        return savedEnrollment;
    }

    //32회 수업 날짜 계산
    private List<LocalDate> calculateSessionDates(LocalDate startDate, int classDay, int countSession) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;
        
        //classDay (1: 월수금, 2: 화목)
        List<DayOfWeek> targetDays = new ArrayList<>();
        if (classDay == 1) {
            targetDays.add(DayOfWeek.MONDAY);
            targetDays.add(DayOfWeek.WEDNESDAY);
            targetDays.add(DayOfWeek.FRIDAY);
        } else {
            targetDays.add(DayOfWeek.TUESDAY);
            targetDays.add(DayOfWeek.THURSDAY);
        }

        while (dates.size() < countSession) {
            DayOfWeek day = currentDate.getDayOfWeek();
            
            //주말이 아니고 targetDays에 포함되면
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                if (targetDays.contains(day)) {
                    dates.add(currentDate);
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        return dates;
    }
}