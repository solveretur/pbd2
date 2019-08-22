package com.automatyka.pbd.achievements;

import com.automatyka.pbd.run_results.Season;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AchievementsController {

    private final AcheviementsRepo acheviementsRepo;


    @GetMapping("/results/{athlete_id}/achievements")
    public String getAchievements(final Model model, @PathVariable("athlete_id") final long athleteId) {
        val achievements = acheviementsRepo.getAchievements(athleteId);
        val achievementsMap = toRunResultsBySeasonMap(achievements);
        model.addAttribute("resultsMap", achievementsMap);
        return "achievements";
    }


    Multimap<Season, Achievement> toRunResultsBySeasonMap(final List<Achievement> achievements) {
        Multimap<Season, Achievement> results = MultimapBuilder
                .treeKeys(Comparator.comparing(Season::getYear).reversed())
                .arrayListValues()
                .build();
        for (final Achievement result : achievements) {
            results.put(result.getSeason(), result);
        }
        return results;
    }
}
