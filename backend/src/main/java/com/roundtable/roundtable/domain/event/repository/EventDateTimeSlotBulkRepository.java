package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional
public class EventDateTimeSlotBulkRepository {

    private final JdbcTemplate jdbcTemplate;


    public void saveAll(List<EventDateTimeSlot> eventDateTimeSlots) {
        String sql = """
                INSERT INTO event_date_time_slot (
                    event_id, start_time, is_completed, is_skipped, created_at, updated_at
                )
                VALUES (?, ?, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                """;

        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        EventDateTimeSlot eventDateTimeSlot = eventDateTimeSlots.get(i);
                        ps.setLong(1, eventDateTimeSlot.getEvent().getId());
                        ps.setTimestamp(2, Timestamp.valueOf(eventDateTimeSlot.getStartTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return eventDateTimeSlots.size();
                    }
                });
    }
}
