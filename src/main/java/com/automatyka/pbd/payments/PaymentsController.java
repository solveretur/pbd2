package com.automatyka.pbd.payments;

import com.automatyka.pbd.event_register.EventRegistrationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PaymentsController {
    private final EventRegistrationRepo eventRegistrationRepo;
    private final PaymentsRepo paymentsRepo;

    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityService.isOwner(#personId, principal)")
    @PostMapping("/payments")
    public String doPayment(@RequestParam("eventId") long eventId, @RequestParam("personId") final long personId, RedirectAttributes redirectAttributes) {
        final boolean paymentWasPaid = eventRegistrationRepo.checkIfPaymentWasPaid(eventId, personId);
        final boolean eventContestantValid = eventRegistrationRepo.isEventContestantValid(eventId, personId);
        if (paymentWasPaid && eventContestantValid) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("text", "Już zapłacono");
            return "redirect:/events/" + eventId + "?athleteId=" + personId;
        }
        final int status = paymentsRepo.doPayment(eventId, personId);
        if (status == 0) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("text", "Nie możesz dokonać opłaty");
            return "redirect:/events/" + eventId + "?athleteId=" + personId;
        }
        redirectAttributes.addAttribute("success", true);
        redirectAttributes.addAttribute("text", "Dokonano opłaty");
        return "redirect:/events/" + eventId + "?athleteId=" + personId;
    }

    /*@GetMapping("/payments")
    public String getPayments(Model model, @RequestParam("eventId") long eventId, @RequestParam("personId") final long personId, RedirectAttributes redirectAttributes) {
        final boolean paymentWasPaid = eventRegistrationRepo.checkIfPaymentWasPaid(eventId, personId);
        final boolean eventContestantValid = eventRegistrationRepo.isEventContestantValid(eventId, personId);
        if (paymentWasPaid && eventContestantValid) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("text", "Już zapłacono");
            return "redirect:/events/" + eventId + "?athleteId=" + personId;
        }
        val paymentExists = eventRegistrationRepo.checkIfPaymentExists(eventId, personId);
        if (!paymentExists) {
            redirectAttributes.addAttribute("error", true);
            redirectAttributes.addAttribute("text", "Nie możesz dokonać opłaty");
            return "redirect:/events/" + eventId + "?athleteId=" + personId;
        }
        model.addAttribute("registration_fee", paymentsRepo.getPaymentFee(eventId));
        return "payment";
    }
*/
}

