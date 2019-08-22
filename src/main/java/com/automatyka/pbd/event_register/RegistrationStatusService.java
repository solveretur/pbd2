package com.automatyka.pbd.event_register;

import com.automatyka.pbd.event.Event;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RegistrationStatusService {

    private final EventRegistrationRepo eventRegistrationRepo;

    public Event setRegisterStatuses(final Event event, final long athleteId) {
        val now = LocalDate.now();
        val registrationStatus = eventRegistrationRepo.getRegistrationStatus(event.getEventId(), athleteId);
        if (registrationStatus == EventRegistrationStatus.NONE) {
            if (event.getOccurDate().isBefore(now) || event.getCurrentNumberOfContestants() >= event.getMaxNumberOfContestants()) {
                return event
                        .withShouldPaymentButtonBeDisplayed(false)
                        .withShouldRegisterButtonBeEnabled(false)
                        .withDisplayRegistrationStatus(false);
            }
            return event
                    .withShouldPaymentButtonBeDisplayed(false)
                    .withShouldRegisterButtonBeEnabled(true)
                    .withDisplayRegistrationStatus(false);
        }
        if (registrationStatus == EventRegistrationStatus.REGISTERED || registrationStatus == EventRegistrationStatus.REJECTED || registrationStatus == EventRegistrationStatus.WAITING_FOR_ACCEPTANCE) {
            return event
                    .withShouldPaymentButtonBeDisplayed(false)
                    .withShouldRegisterButtonBeEnabled(false)
                    .withDisplayRegistrationStatus(true)
                    .withRegistrationStatus(registrationStatus.toString());
        }
        if (registrationStatus == EventRegistrationStatus.WAITING_FOR_PAYMENT) {
            if (event.getOccurDate().isBefore(now)) {
                return event
                        .withShouldPaymentButtonBeDisplayed(false)
                        .withShouldRegisterButtonBeEnabled(false)
                        .withDisplayRegistrationStatus(true)
                        .withRegistrationStatus("Minął termin zapłaty");
            }
            if (event.getCurrentNumberOfContestants() != null && event.getCurrentNumberOfContestants() >= event.getMaxNumberOfContestants()) {
                return event
                        .withShouldPaymentButtonBeDisplayed(false)
                        .withShouldRegisterButtonBeEnabled(false)
                        .withDisplayRegistrationStatus(true)
                        .withRegistrationStatus("Limit osób osiągnięty");
            }
            return event
                    .withShouldPaymentButtonBeDisplayed(true)
                    .withShouldRegisterButtonBeEnabled(false)
                    .withDisplayRegistrationStatus(true)
                    .withRegistrationStatus(registrationStatus.toString());
        }
        return event;
    }

}
