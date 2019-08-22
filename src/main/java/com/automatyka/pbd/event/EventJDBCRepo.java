package com.automatyka.pbd.event;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
class EventJDBCRepo implements EventRepo {

    private static final String GET_ALL_EVENTS_BETWEEN_DATES_QUERY = "SELECT e.event_id, er.event_rank, e.town, e.district, e.occur_date, e.max_number_of_contestants FROM events AS e JOIN event_ranks_dict AS er ON er.event_rank_id = e.event_rank_id WHERE e.occur_date BETWEEN ? AND ?";
    private static final String GET_BY_EVENT_ID = "SELECT e.event_id, er.event_rank, e.town, e.district, e.occur_date, e.max_number_of_contestants, count(ec.contestant_id) AS current_number_of_contestants FROM events AS e JOIN event_ranks_dict AS er ON er.event_rank_id = e.event_rank_id JOIN event_contestants AS ec ON ec.event_id = e.event_id WHERE e.event_id = ? AND ec.is_valid_for_run=1";
    private static final String GET_ALL_EVENTS_FOR_ATHLETE_ID = "SELECT e.event_id, er.event_rank, e.town, e.district, e.occur_date FROM events AS e JOIN event_ranks_dict AS er ON er.event_rank_id = e.event_rank_id JOIN event_contestants AS ec ON ec.event_id = e.event_id WHERE ec.athlete_id=? ORDER BY e.occur_date DESC";


    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getAllEventsBetweenDatesForCalendar(final LocalDate from, final LocalDate to) {
        return jdbcTemplate.query(GET_ALL_EVENTS_BETWEEN_DATES_QUERY, new Object[]{from, to}, EventResultSetExtractors::extractDataForCalendar);
    }

    @Override
    public Event getByEventId(final long eventId) {
        return jdbcTemplate.query(GET_BY_EVENT_ID, new String[]{String.valueOf(eventId)}, EventResultSetExtractors::extractDataForEvent);
    }

    @Override
    public List<Event> getAllEventsForAthlete(final long athleteId) {
        return jdbcTemplate.query(GET_ALL_EVENTS_FOR_ATHLETE_ID, new String[]{String.valueOf(athleteId)}, EventResultSetExtractors::extractDataForAthleteEvents);
    }
}
