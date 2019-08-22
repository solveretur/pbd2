package com.automatyka.pbd.event;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.time.LocalDate;

@Value
@Builder
@Wither
public class Event {
    Long eventId;
    String eventRank;
    String town;
    String district;
    LocalDate occurDate;
    Integer maxNumberOfContestants;
    Integer currentNumberOfContestants;
    boolean shouldRegisterButtonBeEnabled;
    boolean shouldPaymentButtonBeDisplayed;
    boolean displayRegistrationStatus;
    String registrationStatus;
}
