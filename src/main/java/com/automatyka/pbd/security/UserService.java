package com.automatyka.pbd.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepo appUserRepo;

    public AppUser findByUsername(final String username) {
        return appUserRepo.findByUsername(username);
    }
}
