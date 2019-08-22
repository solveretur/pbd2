package com.automatyka.pbd.profile;

import com.automatyka.pbd.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class AthleteProfileController {
    private final ProfileService profileService;

    @GetMapping("/me")
    public String getMyProfile(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        val user = profileService.getMyProfile(currentUser.getId());
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "me";
    }

    @GetMapping("/athlethes/{id}")
    public String getUserProfile(Model model, @PathVariable Long id, @AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser.getId().equals(id)) {
            return "redirect:/me";
        }
        val user = profileService.getMyProfile(id);
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user.withId(id));
        return "profile";
    }
}
