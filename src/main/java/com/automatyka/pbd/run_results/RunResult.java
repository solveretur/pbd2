package com.automatyka.pbd.run_results;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
public class RunResult {
    int eventId;
    String eventRank;
    String runType;
    LocalTime result;
    LocalDate occurDate;
    String town;
    Season season;
}
