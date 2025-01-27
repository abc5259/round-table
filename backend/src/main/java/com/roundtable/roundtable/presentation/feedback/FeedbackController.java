package com.roundtable.roundtable.presentation.feedback;

import com.roundtable.roundtable.business.feedback.FeedbackService;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.presentation.feedback.request.CreateFeedbackRequest;
import com.roundtable.roundtable.presentation.feedback.response.CreateFeedbackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "집안일 피드백 전송", description = "집안일에 대한 피드백을 전송합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping
    public ResponseEntity<ResponseDto<CreateFeedbackResponse>> createFeedback(
            @PathVariable Long houseId,
            @Valid @RequestBody CreateFeedbackRequest createFeedbackRequest
    ) {
        Long feedbackId = feedbackService.createFeedback(createFeedbackRequest.toCreateFeedbackServiceDto(), houseId);
        return ResponseEntity.ok(SuccessResponse.from(new CreateFeedbackResponse(feedbackId)));
    }
}
