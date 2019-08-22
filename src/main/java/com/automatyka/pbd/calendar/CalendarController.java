package com.automatyka.pbd.calendar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public final class CalendarController {

    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }
}
