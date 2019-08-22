package com.automatyka.pbd.profile;

import com.automatyka.pbd.run_results.RunResultRepo;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final RunResultRepo runResultRepo;
    private final ProfileRepo profileRepo;

    public AthleteProfile getMyProfile(final long athleteId) {
        val user = profileRepo.getProfileForMyProfile(athleteId);
        val bestResults = runResultRepo.getBestRunResultsByAthleteId(athleteId);
        return Optional.ofNullable(user).map(u -> u.withRecords(bestResults)).orElse(null);
    }
}
