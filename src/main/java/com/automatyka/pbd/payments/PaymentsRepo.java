package com.automatyka.pbd.payments;

import com.automatyka.pbd.event.EventRepo;
import com.automatyka.pbd.event_register.EventRegistrationRepo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentsRepo {
    private static final String MAKE_CONTESTANT_VALID = "UPDATE event_contestants SET is_valid_for_run=1, run_number=? WHERE event_id=? AND athlete_id=?";
    private static final String DO_PAYMENT = "UPDATE payments SET accounting_date=? WHERE event_id=? AND athlete_id=?";
//    private static final String GET_PAYMENT_FEE = "SELECT e.registration_fee FROM events WHERE e.event_id = ?";


    private final JdbcTemplate jdbcTemplate;
    private final EventRepo eventRepo;
    private final EventRegistrationRepo eventRegistrationRepo;

    /*public int getPaymentFee(final long eventId) {
        final Integer count = jdbcTemplate.queryForObject(GET_PAYMENT_FEE, new Object[]{eventId}, Integer.class);
        return Optional.ofNullable(count).orElse(0);
    }*/

    @Transactional
    public int doPayment(final long eventId, final long athleteId) {
        val event = eventRepo.getByEventId(eventId);
        if (event == null || event.getCurrentNumberOfContestants() >= event.getMaxNumberOfContestants()) {
            return 0;
        }
        final boolean paymentExists = eventRegistrationRepo.checkIfPaymentExists(eventId, athleteId);
        final boolean paymentWasPaid = eventRegistrationRepo.checkIfPaymentWasPaid(eventId, athleteId);
        final boolean eventContestantExists = eventRegistrationRepo.checkIfEventContestantExists(eventId, athleteId);
        final boolean eventContestantValid = eventRegistrationRepo.isEventContestantValid(eventId, athleteId);
        if (paymentExists && !paymentWasPaid && eventContestantExists && !eventContestantValid) {
            val now = LocalDate.now();
            val status = jdbcTemplate.update(DO_PAYMENT, now, eventId, athleteId);
            if (status != 0) {
                return jdbcTemplate.update(MAKE_CONTESTANT_VALID, UUID.randomUUID().toString().substring(0, 5), eventId, athleteId);
            }
            throw new RuntimeException("Payment not successful");
        }
        return 0;
    }
}
