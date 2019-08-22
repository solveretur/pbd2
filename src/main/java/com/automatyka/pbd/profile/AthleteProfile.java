package com.automatyka.pbd.profile;

import com.automatyka.pbd.run_results.RunResult;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder
@Wither
public class AthleteProfile {
    long id;
    String username;
    String email;
    String name;
    String surname;
    LocalDate birthday;
    String town;
    String sex;
    boolean professional;
    LocalDate careerStartDate;
    LocalDate careerEndDate;
    boolean active;
    String teamName;
    String licence;
    String sportClass;
    Integer trainingPlanId;
    String coachName;
    String coachSurname;
    List<RunResult> records;
}
