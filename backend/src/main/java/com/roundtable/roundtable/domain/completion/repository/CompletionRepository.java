package com.roundtable.roundtable.domain.completion.repository;

import com.roundtable.roundtable.domain.completion.Completion;
import com.roundtable.roundtable.domain.completion.dto.CompletionStatisticsDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompletionRepository extends JpaRepository<Completion, Long> {

    @Query("""
               select new com.roundtable.roundtable.domain.completion.dto.CompletionStatisticsDto(c.category, sum(c.id))
               from Completion c
               where c.member.id = :memberId and c.completedDate >= :startDate and c.completedDate <= :endDate
               group by c.category
            """)
    List<CompletionStatisticsDto> findCompletionStatisticsByMemberIdAndBetweenDates(Long memberId,
                                                                                    LocalDate startDate,
                                                                                    LocalDate endDate);
}
