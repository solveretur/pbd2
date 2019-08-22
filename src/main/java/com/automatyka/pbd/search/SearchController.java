package com.automatyka.pbd.search;

import com.automatyka.pbd.start_list.AthleteProfileDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final Search search;

    @GetMapping("/search/search_result")
    public String getAthletes(final Model model, @RequestParam("name") String name, @RequestParam("surname") String surname) {
        final List<AthleteProfileDTO> athletes = search.getList(name, surname);
        if (athletes.isEmpty()) {
            return "redirect:/search?error=Uzykownik+o+podanym+imieniu+i+nazwisku+nie+istnieje";
        }
        model.addAttribute("people", athletes);
        return "search_result";
    }

    @GetMapping("/search")
    public String getSearchQuery(final Model model) {
        model.addAttribute("searchForm", new SearchQuery());
        return "search";
    }

    @PostMapping("/search/process")
    public String processSearchQuery(@ModelAttribute("searchForm") final SearchQuery searchQuery) {
        if (StringUtils.isBlank(searchQuery.getSurname()) || StringUtils.isBlank(searchQuery.getName())) {
            return "redirect:/search?error=Pola+nie+moga+byc+puste";
        }
        return "redirect:/search/search_result?name=" + searchQuery.getName() + "&surname=" + searchQuery.getSurname();
    }
}