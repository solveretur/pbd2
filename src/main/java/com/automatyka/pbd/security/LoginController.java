package com.automatyka.pbd.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserService userService;
    private final SecurityService securityService;

    @GetMapping(value = "/login")
    public String getLoginPage(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("error", error);
        return "login";
    }
}
