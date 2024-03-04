package com.roundtable.roundtable.implement.schdulecomment;

import com.roundtable.roundtable.entity.schedulecomment.Content;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleComment;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ScheduleCommentAppender {

    private final ScheduleCommentRepository scheduleCommentRepository;

    public ScheduleComment append(CreateScheduleComment createScheduleComment) {
        ScheduleComment scheduleComment = ScheduleComment.create(
                Content.of(createScheduleComment.content()),
                createScheduleComment.schedule(),
                createScheduleComment.writer()
        );

        return scheduleCommentRepository.save(scheduleComment);
    }
}
