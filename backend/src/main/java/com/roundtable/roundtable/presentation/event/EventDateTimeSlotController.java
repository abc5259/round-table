package com.roundtable.roundtable.presentation.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.EventDateTimeSlotService;
import com.roundtable.roundtable.business.event.dto.CompleteEventDto;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.event.response.CompletedEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house/{houseId}/event-date-time-slot")
@RequiredArgsConstructor
public class EventDateTimeSlotController {

    private final EventDateTimeSlotService eventDateTimeSlotService;

    @Operation(summary = "이벤트 완료", description = "이벤트를 완료합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/{eventDateTimeSlotId}/complete")
    public ResponseEntity<SuccessResponse<CompletedEventResponse>> createEvent(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @PathVariable Long eventDateTimeSlotId
    ) {
        eventDateTimeSlotService.completeEvent(new CompleteEventDto(eventDateTimeSlotId),
                authMember.toHouseAuthMember(houseId));

        return ResponseEntity.ok()
                .body(SuccessResponse.from(new CompletedEventResponse(eventDateTimeSlotId)));
    }

    @Operation(summary = "날짜에 해당하는 이벤트 조회", description = "날짜에 해당하는 하우스에서 해야할 이벤트 조회")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    public ResponseEntity<SuccessResponse<List<EventDateTimeSlotDetailDto>>> createEvent(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @RequestParam("datetime") LocalDateTime dateTime
    ) {

        List<EventDateTimeSlotDetailDto> eventDateTimeSlotDetailDtos = eventDateTimeSlotService.findEventDateTimeSlotDetails(
                authMember.toHouseAuthMember(houseId),
                dateTime);
        return ResponseEntity.ok()
                .body(SuccessResponse.from(eventDateTimeSlotDetailDtos));
    }
}
