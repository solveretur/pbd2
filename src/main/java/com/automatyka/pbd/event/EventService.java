package com.automatyka.pbd.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class EventService {

    private final EventRepo eventRepo;

    public List<Event> getAllBetweenDatesForCalendar(final LocalDate from, final LocalDate to) {
        return eventRepo.getAllEventsBetweenDatesForCalendar(from, to);
    }
}
