package com.roundtable.roundtable.presentation.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.EventService;
import com.roundtable.roundtable.business.event.dto.CompleteEventDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.event.request.CreateEventRequest;
import com.roundtable.roundtable.presentation.event.response.CompletedEventResponse;
import com.roundtable.roundtable.presentation.event.response.CreateEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house/{houseId}/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 생성", description = "이벤트를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping
    public ResponseEntity<SuccessResponse<CreateEventResponse>> createEvent(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @RequestBody @Valid CreateEventRequest createEventRequest
    ) {
        Long eventId = eventService.createEvent(
                createEventRequest.toCreateEventDto(),
                authMember.toHouseAuthMember(houseId));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(SuccessResponse.from(new CreateEventResponse(eventId)));
    }

    @Operation(summary = "이벤트 완료", description = "이벤트를 완료합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/event-date-time-slot/{eventDateTimeSlotId}/complete")
    public ResponseEntity<SuccessResponse<CompletedEventResponse>> createEvent(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @PathVariable Long eventDateTimeSlotId
    ) {
        eventService.completeEvent(new CompleteEventDto(eventDateTimeSlotId), authMember.toHouseAuthMember(houseId));

        return ResponseEntity.ok()
                .body(SuccessResponse.from(new CompletedEventResponse(eventDateTimeSlotId)));
    }
}
