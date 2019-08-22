package com.automatyka.pbd.event;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@UtilityClass
class EventResultSetExtractors {

    List<Event> extractDataForCalendar(final ResultSet rs) throws SQLException {
        final List<Event> events = new LinkedList<>();
        while (rs.next()) {
            val eventId = rs.getInt("event_id");
            val event_rank = EventRank.valueOf(rs.getString("event_rank").trim());
            val town = rs.getString("town");
            val district = rs.getString("district");
            val occurDate = LocalDate.parse(rs.getString("occur_date"));
            val maxNumberOfContestants = rs.getInt("max_number_of_contestants");
            val event = Event.builder()
                    .eventId((long) eventId)
                    .eventRank(event_rank.toString())
                    .town(town)
                    .district(district)
                    .occurDate(occurDate)
                    .maxNumberOfContestants(maxNumberOfContestants)
                    .build();
            events.add(event);
        }
        return events;
    }

    Event extractDataForEvent(final ResultSet rs) throws SQLException {
        if (rs.next()) {
            val eventId = rs.getInt("event_id");
            val event_rank = EventRank.valueOf(rs.getString("event_rank").trim());
            val town = rs.getString("town");
            val district = rs.getString("district");
            val occurDate = LocalDate.parse(rs.getString("occur_date"));
            val maxNumberOfContestants = rs.getInt("max_number_of_contestants");
            val currentNumberOfContestants = rs.getInt("current_number_of_contestants");
            return Event.builder()
                    .eventId((long) eventId)
                    .eventRank(event_rank.toString())
                    .town(town)
                    .district(district)
                    .occurDate(occurDate)
                    .maxNumberOfContestants(maxNumberOfContestants)
                    .currentNumberOfContestants(currentNumberOfContestants)
                    .shouldRegisterButtonBeEnabled(true)
                    .shouldPaymentButtonBeDisplayed(false)
                    .displayRegistrationStatus(false)
                    .registrationStatus("")
                    .build();
        }
        return null;
    }

    List<Event> extractDataForAthleteEvents(final ResultSet rs) throws SQLException {
        final List<Event> events = new LinkedList<>();
        while (rs.next()) {
            val eventId = rs.getInt("event_id");
            val event_rank = EventRank.valueOf(rs.getString("event_rank").trim());
            val town = rs.getString("town");
            val district = rs.getString("district");
            val occurDate = LocalDate.parse(rs.getString("occur_date"));
            val event = Event.builder()
                    .eventId((long) eventId)
                    .eventRank(event_rank.toString())
                    .town(town)
                    .district(district)
                    .occurDate(occurDate)
                    .build();
            events.add(event);
        }
        return events;
    }
}
