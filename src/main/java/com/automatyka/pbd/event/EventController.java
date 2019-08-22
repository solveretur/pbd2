package com.automatyka.pbd.event;

import com.automatyka.pbd.event_register.RegistrationStatusService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventRepo eventRepo;
    private final RegistrationStatusService registrationStatusService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityService.isOwner(#athleteId, principal)")
    @GetMapping("/events/{id}")
    public String getEventById(Model model, @PathVariable("id") long id, @RequestParam("athleteId") long athleteId) {
        val event = registrationStatusService.setRegisterStatuses(eventRepo.getByEventId(id), athleteId);
        model.addAttribute("event", event);
        return "event";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityService.isOwner(#athleteId, principal)")
    @GetMapping("/events/user/{athlete_id}")
    public String getEventsForUser(Model model, @PathVariable("athlete_id") long athleteId) {
        val events = eventRepo.getAllEventsForAthlete(athleteId).stream().filter(Objects::nonNull).map(e -> registrationStatusService.setRegisterStatuses(e, athleteId)).collect(Collectors.toList());
        model.addAttribute("events", events);
        return "my_events";
    }
}
