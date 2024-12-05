package com.roundtable.roundtable.presentation.completion;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.completion.CompletionService;
import com.roundtable.roundtable.domain.completion.dto.CompletionStatisticsDto;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/house/{houseId}/completions")
@RestController
@RequiredArgsConstructor
public class CompletionController {

    private final CompletionService completionService;

    @Operation(summary = "내 집안일 완료 통계 목록 조회", description = "내가 완료한 집안일 완료 통계를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<List<CompletionStatisticsDto>>> findCompletionStatisticsByMemberIdAndBetweenDates(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        List<CompletionStatisticsDto> result = completionService.findCompletionStatisticsByMemberIdAndBetweenDates(
                authMember.toHouseAuthMember(houseId),
                startDate,
                endDate
        );

        return ResponseEntity.ok(SuccessResponse.from(result));
    }
}
