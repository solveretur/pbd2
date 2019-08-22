package com.automatyka.pbd.calendar;

import com.automatyka.pbd.event.EventService;
import com.automatyka.pbd.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public final class FullCalendarEventsRestController {

    private final EventService eventService;
    private final FullCalendarEventDTOFactory fullCalendarEventDTOFactory = new FullCalendarEventDTOFactory();

    @GetMapping("/events/fullcalendar")
    public ResponseEntity<List<FullCalendarEventDTO>> getAllBetweenDates(@RequestParam("start") String start, @RequestParam("end") String end, @AuthenticationPrincipal CurrentUser currentUser) {
        val from = LocalDate.parse(start);
        val to = LocalDate.parse(end);
        val events = eventService.getAllBetweenDatesForCalendar(from, to).stream().map(e -> fullCalendarEventDTOFactory.create(e, String.valueOf(currentUser.getId()))).collect(Collectors.toList());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

}
