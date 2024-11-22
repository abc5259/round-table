package com.roundtable.roundtable.presentation.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.EventService;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.event.request.CreateEventRequest;
import com.roundtable.roundtable.presentation.event.response.CreateEventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
