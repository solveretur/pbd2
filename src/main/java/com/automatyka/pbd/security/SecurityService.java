package com.automatyka.pbd.security;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public boolean isOwner(final long id, final CurrentUser currentUser) {
        return currentUser.getId().equals(id);
    }
}
