package com.automatyka.pbd.event;

import java.time.LocalDate;
import java.util.List;

public interface EventRepo {
    List<Event> getAllEventsBetweenDatesForCalendar(final LocalDate from, final LocalDate to);

    Event getByEventId(final long eventId);

    List<Event> getAllEventsForAthlete(final long athleteId);
}
