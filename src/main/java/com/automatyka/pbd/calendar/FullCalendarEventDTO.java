package com.automatyka.pbd.calendar;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
class FullCalendarEventDTO {
    String id;
    String title;
    boolean allDay = true;
    LocalDate start;
    LocalDate end;
    String url;
    boolean editable = false;
}
