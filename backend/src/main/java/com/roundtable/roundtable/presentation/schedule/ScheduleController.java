package com.roundtable.roundtable.presentation.schedule;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.schedule.ScheduleCompletionService;
import com.roundtable.roundtable.business.schedule.ScheduleService;
import com.roundtable.roundtable.business.schedule.dto.ScheduleOfMemberResponse;
import com.roundtable.roundtable.business.schedule.dto.ScheduleResponse;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.common.request.CursorBasedPaginationRequest;
import com.roundtable.roundtable.presentation.schedule.request.CreateOneTimeScheduleRequest;
import com.roundtable.roundtable.presentation.schedule.request.CreateScheduleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house/{houseId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final ScheduleCompletionService scheduleCompletionService;

    @Operation(summary = "반복 스케줄 생성", description = "반복 스케줄을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping("/repeat")
    public ResponseEntity<ResponseDto<Long>> createRepeatSchedule(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @Valid @RequestBody CreateScheduleRequest createScheduleRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.from(
                        scheduleService.create(createScheduleRequest.toCreateScheduleDto(),
                                authMember.toHouseAuthMember(houseId))
                )
        );
    }

    @Operation(summary = "일회성 스케줄 생성", description = "일회성 스케줄을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping("/one-time")
    public ResponseEntity<ResponseDto<Long>> createOneTimeSchedule(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @Valid @RequestBody CreateOneTimeScheduleRequest createOneTimeScheduleRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.from(
                        scheduleService.create(createOneTimeScheduleRequest.toCreateScheduleDto(),
                                authMember.toHouseAuthMember(houseId))
                )
        );
    }

    @Operation(summary = "내 집안일 조회", description = "오늘 해야할 내 집안일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<CursorBasedResponse<List<ScheduleOfMemberResponse>>>> findSchedulesOfLoginUser(
            @Login AuthMember authMember,
            @PathVariable("houseId") Long houseId,
            @RequestParam(required = false) LocalDate date,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
    ) {
        if (date == null) {
            date = LocalDate.now();
        }
        var response = scheduleService.findMemberSchedulesByDate(authMember.toHouseAuthMember(houseId), date,
                cursorBasedPaginationRequest.toCursorBasedRequest());
        return ResponseEntity.ok(SuccessResponse.from(response));
    }

    @Operation(summary = "하우스 집안일 조회", description = "오늘 해야할 하우스 집안일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    public ResponseEntity<ResponseDto<CursorBasedResponse<List<ScheduleResponse>>>> findSchedulesOfHome(
            @Login AuthMember authMember,
            @PathVariable("houseId") Long houseId,
            @RequestParam(required = false) LocalDate date,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
    ) {
        if (date == null) {
            date = LocalDate.now();
        }
        var response = scheduleService.findSchedulesByDate(authMember.toHouseAuthMember(houseId), date,
                cursorBasedPaginationRequest.toCursorBasedRequest());
        return ResponseEntity.ok(SuccessResponse.from(response));
    }

    @Operation(summary = "스케줄 완료", description = "스케줄을 완료합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/{scheduleId}/complete")
    public ResponseEntity<ResponseDto<Void>> completeSchedule(
            @Login AuthMember authMember,
            @PathVariable("houseId") Long houseId,
            @PathVariable("scheduleId") Long scheduleId
    ) {
        scheduleCompletionService.complete(scheduleId, authMember.toHouseAuthMember(houseId), LocalDate.now());

        return ResponseEntity.ok(SuccessResponse.ok());
    }
    
}
