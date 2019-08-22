package com.automatyka.pbd.start_list;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class StartListController {

    private final StartListRepo startListRepo;

    @GetMapping("/events/{event_id}/start_list")
    public String getStartList(final Model model, @PathVariable("event_id") long eventId) {
        model.addAttribute("event_id", eventId);
        model.addAttribute("athletes", startListRepo.getStartListForEvent(eventId));
        return "start_list";
    }
}
