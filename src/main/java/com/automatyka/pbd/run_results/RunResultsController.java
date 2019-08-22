package com.automatyka.pbd.run_results;

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
public class RunResultsController {
    private final RunResultRepo runResultRepo;

    @GetMapping("/results/{athlete_id}")
    public String getResults(Model model, @PathVariable("athlete_id") long athleteId) {
        val runResults = runResultRepo.getRunResultsByAthleteId(athleteId);
        val resultsMap = toRunResultsBySeasonMap(runResults);
        model.addAttribute("resultsMap", resultsMap);
        return "results";
    }

    Multimap<Season, RunResult> toRunResultsBySeasonMap(final List<RunResult> runResult) {
        Multimap<Season, RunResult> results = MultimapBuilder.treeKeys(Comparator.comparing(Season::getYear).reversed()).arrayListValues().build();
        for (final RunResult result : runResult) {
            results.put(result.getSeason(), result);
        }
        return results;
    }
}
