package com.automatyka.pbd.achievements;

import com.automatyka.pbd.run_results.Season;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
@Wither
public class Achievement {
    long athleteId;
    long runResultId;
    String runType;
    String runTypeString;
    int resultInNumberOfSeconds;
    LocalTime result;
    Season season;
    Long eventId;
    String eventRank;
    String town;
    String district;
    LocalDate occurDate;
    int position;
    long runTypeId;
}
