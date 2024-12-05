package com.roundtable.roundtable.business.completion;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.domain.completion.dto.CompletionStatisticsDto;
import com.roundtable.roundtable.domain.completion.repository.CompletionRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompletionService {

    private final CompletionRepository completionRepository;

    public List<CompletionStatisticsDto> findCompletionStatisticsByMemberIdAndBetweenDates(
            AuthMember authMember,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return completionRepository.findCompletionStatisticsByMemberIdAndBetweenDates(
                authMember.memberId(),
                startDate,
                endDate
        );
    }
}
