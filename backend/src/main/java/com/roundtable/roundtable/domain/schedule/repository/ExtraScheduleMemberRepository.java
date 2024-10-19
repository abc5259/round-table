package com.roundtable.roundtable.domain.schedule.repository;

import com.roundtable.roundtable.domain.schedule.ExtraScheduleMember;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtraScheduleMemberRepository extends JpaRepository<ExtraScheduleMember, Long> {
    List<ExtraScheduleMember> findByScheduleIdAndAssignedDate(Long scheduleId, LocalDate assignedDate);
}
