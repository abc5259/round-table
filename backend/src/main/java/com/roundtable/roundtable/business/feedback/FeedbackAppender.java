package com.roundtable.roundtable.business.feedback;

import static com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode.NOT_FOUND_PREDEFINED_FEEDBACK;

import com.roundtable.roundtable.business.feedback.dto.CreateFeedback;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.feedback.FeedbackRepository;
import com.roundtable.roundtable.domain.feedback.FeedbackSelection;
import com.roundtable.roundtable.domain.feedback.FeedbackSelectionRepository;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedback;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedbackRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class FeedbackAppender {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackSelectionRepository feedbackSelectionRepository;
    private final PredefinedFeedbackRepository predefinedFeedbackRepository;

    public Feedback append(CreateFeedback createFeedback) {
        List<PredefinedFeedback> predefinedFeedbacks = predefinedFeedbackRepository.findByIdIn(
                createFeedback.predefinedFeedbackIds());
        validateAllPredefinedFeedbacksExist(createFeedback, predefinedFeedbacks);

        Feedback feedback = feedbackRepository.save(
                Feedback.create(createFeedback.emoji(), createFeedback.message(), createFeedback.eventDateTimeSlot(),
                        createFeedback.sender(), createFeedback.eventParticipants())
        );
        appendFeedbackSelections(predefinedFeedbacks, feedback);

        return feedback;
    }

    private void validateAllPredefinedFeedbacksExist(CreateFeedback createFeedback,
                                                     List<PredefinedFeedback> predefinedFeedbacks) {
        if (createFeedback.predefinedFeedbackIds().size() != predefinedFeedbacks.size()) {
            throw new NotFoundEntityException(NOT_FOUND_PREDEFINED_FEEDBACK);
        }
    }

    private void appendFeedbackSelections(List<PredefinedFeedback> predefinedFeedbacks, Feedback feedback) {
        List<FeedbackSelection> feedbackSelections = predefinedFeedbacks.stream()
                .map(predefinedFeedback -> FeedbackSelection.create(feedback, predefinedFeedback)).toList();

        feedbackSelectionRepository.saveAll(feedbackSelections);
    }
}
