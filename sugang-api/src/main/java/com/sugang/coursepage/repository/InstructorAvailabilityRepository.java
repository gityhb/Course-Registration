package com.sugang.coursepage.repository;

import com.sugang.coursepage.entity.InstructorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InstructorAvailabilityRepository extends JpaRepository<InstructorAvailability, Long> {
    List<InstructorAvailability> findByInstructorId(String instructorId);
}