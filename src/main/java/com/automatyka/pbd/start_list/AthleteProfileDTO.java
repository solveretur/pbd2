package com.automatyka.pbd.start_list;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AthleteProfileDTO {
    int id;
    String name;
    String surname;
    String runNumber;
    String birthYear;
    String team;
    String town;
    String isPaid;
}
