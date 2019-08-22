package com.automatyka.pbd.calendar;

import com.automatyka.pbd.event.Event;
import lombok.val;

import java.util.UUID;

final class FullCalendarEventDTOFactory {

    FullCalendarEventDTO create(final Event event, final String athleteId) {
        val id = UUID.randomUUID().toString();
        val title = titleCreator(event);
        val start = event.getOccurDate();
        val end = event.getOccurDate();
        val url = "/events/" + event.getEventId() + "?athleteId=" + athleteId;
        return new FullCalendarEventDTO(id, title, start, end, url);
    }

    private String titleCreator(final Event event) {
        return event.getTown() + ", " + event.getEventRank();
    }

}
