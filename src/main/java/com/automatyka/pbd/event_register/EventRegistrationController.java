package com.automatyka.pbd.event_register;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EventRegistrationController {
    private final EventRegistrationRepo eventRegistrationRepo;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityService.isOwner(#personId, principal)")
    @PostMapping("/events/register")
    public String registerUserForEvent(Model model, @RequestParam("eventId") Long eventId, @RequestParam("personId") Long personId, RedirectAttributes redirectAttributes) {
        if (eventId == null) {
            model.addAttribute("error", "Request Params must not be null");
            return "calendar";
        }
        val status = eventRegistrationRepo.createApplication(eventId, personId);
        if (status == 0) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("text", "Nie spełniasz wymogów");
        } else {
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("text", "Twoja prośba rejestracji została złożona");
        }
        return "redirect:/events/" + eventId + "?athleteId=" + personId;
    }

    /*@PostMapping("/events/register/delete")
    public String deleteUserForEvent(Model model, @RequestParam("eventId") Long eventId, @RequestParam("personId") Long personId, RedirectAttributes redirectAttributes) {
        if (eventId == null) {
            model.addAttribute("error", "Request Params must not be null");
            return "calendar";
        }
        val status = eventRegistrationRepo.createApplication(eventId, personId);
        if (status == 0) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("text", "Nie można usunąć zgłoszenia");
        } else {
            redirectAttributes.addAttribute("success", true);
            redirectAttributes.addAttribute("text", "Zrezygowano z uczestnictwa");
        }
        return "redirect:/events/" + eventId;
    }*/
}
