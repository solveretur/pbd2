package com.automatyka.pbd.event_register;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.var;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventRegistrationRepo {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE_APPLICATION = "INSERT INTO applications(event_id,athlete_id) VALUES (?,?)";
    private static final String CREATE_CONTESTANT = "INSERT INTO event_contestants(event_id, athlete_id) VALUES (?,?)";
    private static final String CREATE_PAYMENT = "INSERT INTO payments(event_id, athlete_id) VALUES (?,?)";

    private static final String CHECK_IF_APPLICATION_EXISTS = "SELECT count(*) FROM applications WHERE event_id = ? AND athlete_id = ?";
    private static final String CHECK_IF_EVENT_CONTESTANTS_EXISTS = "SELECT count(*) FROM event_contestants WHERE event_id = ? AND athlete_id = ?";
    private static final String CHECK_IF_EVENT_CONTESTANTS_IS_VALID = "SELECT count(*) FROM event_contestants AS ec WHERE ec.event_id = ? AND ec.athlete_id = ? AND ec.is_valid_for_run=1";
    private static final String CHECK_IF_PAYMENT_EXISTS = "SELECT count(*) FROM payments WHERE event_id = ? AND athlete_id = ?";
    private static final String CHECK_IF_PAYMENT_WAS_PAID = "SELECT count(*) FROM payments WHERE event_id = ? AND athlete_id = ? AND accounting_date IS NOT NULL";

    private static final String DELETE_APPLICATION = "DELETE FROM applications WHERE event_id = ? AND athlete_id = ?";
    private static final String DELETE_CONTESTANT = "DELETE FROM event_contestants WHERE event_id = ? AND athlete_id = ?";


/*
    private static final String MAKE_CONTESTANT_VALID = "UPDATE event_contestants SET is_valid_for_run=1, run_number=? WHERE event_id=? AND athlete_id=?";
*/

    /*
        @Transactional
        public int makeContestantValid(long eventId, long athleteId, final String runNumber) {
            return jdbcTemplate.update(MAKE_CONTESTANT_VALID, runNumber, eventId, athleteId);
        }
    */

    @Transactional
    public int createApplication(long eventId, long athleteId) {
        if (!checkIfEventContestantExists(eventId, athleteId) && !checkIfApplicationExists(eventId, athleteId)) {
            var status = jdbcTemplate.update(CREATE_APPLICATION, eventId, athleteId);
            if (status == 0) throw new RuntimeException("Couldn't create application");
            status = jdbcTemplate.update(CREATE_CONTESTANT, eventId, athleteId);
            if (status == 0) throw new RuntimeException("Couldn't create application");
            return jdbcTemplate.update(CREATE_PAYMENT, eventId, athleteId);
        }
        return 0;
    }

    /*@Transactional
    public int deleteApplication(long eventId, long athleteId) {
        if (!checkIfApplicationExists(eventId, athleteId)) {
            var status = jdbcTemplate.update(DELETE_APPLICATION, eventId, athleteId);
            if (status == 0) return status;
            return jdbcTemplate.update(DELETE_CONTESTANT, eventId, athleteId);
        }
        return 0;
    }*/

    public boolean checkIfApplicationExists(long eventId, long athleteId) {
        final Integer count = jdbcTemplate.queryForObject(CHECK_IF_APPLICATION_EXISTS, new Object[]{eventId, athleteId}, Integer.class);
        return Optional.ofNullable(count).orElse(0) == 1;
    }

    public boolean checkIfEventContestantExists(long eventId, long athleteId) {
        final Integer count = jdbcTemplate.queryForObject(CHECK_IF_EVENT_CONTESTANTS_EXISTS, new Object[]{eventId, athleteId}, Integer.class);
        return Optional.ofNullable(count).orElse(0) == 1;
    }

    public boolean isEventContestantValid(long eventId, long athleteId) {
        val res = jdbcTemplate.queryForObject(CHECK_IF_EVENT_CONTESTANTS_IS_VALID, new Object[]{eventId, athleteId}, Integer.class);
        return Optional.ofNullable(res).orElse(0) == 1;
    }

    public boolean checkIfPaymentExists(long eventId, long athleteId) {
        val res = jdbcTemplate.queryForObject(CHECK_IF_PAYMENT_EXISTS, new Object[]{eventId, athleteId}, Integer.class);
        return Optional.ofNullable(res).orElse(0) == 1;
    }


    public boolean checkIfPaymentWasPaid(long eventId, long athleteId) {
        val res = jdbcTemplate.queryForObject(CHECK_IF_PAYMENT_WAS_PAID, new Object[]{eventId, athleteId}, Integer.class);
        return Optional.ofNullable(res).orElse(0) == 1;
    }


    @Transactional
    public EventRegistrationStatus getRegistrationStatus(final long eventId, final long athleteId) {
        val applicationExists = checkIfApplicationExists(eventId, athleteId);
        val eventContestantExists = checkIfEventContestantExists(eventId, athleteId);
        val paymentExists = checkIfPaymentExists(eventId, athleteId);
        val wasPaymentPaid = checkIfPaymentWasPaid(eventId, athleteId);
        if (!applicationExists && !eventContestantExists) {
            return EventRegistrationStatus.NONE;
        }
        val isEventContestantValidForRun = isEventContestantValid(eventId, athleteId);
        if (eventContestantExists && isEventContestantValidForRun) {
            return EventRegistrationStatus.REGISTERED;
        }
        if (applicationExists && eventContestantExists && paymentExists && !wasPaymentPaid && !isEventContestantValidForRun) {
            return EventRegistrationStatus.WAITING_FOR_PAYMENT;
        }
        if (applicationExists && eventContestantExists && paymentExists && wasPaymentPaid && !isEventContestantValidForRun) {
            return EventRegistrationStatus.REJECTED;
        }
        throw new RuntimeException("Error registration status");
    }
}
