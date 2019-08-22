package com.automatyka.pbd.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {
    private final AppUser user;

    public CurrentUser(AppUser user) {
        super(user.getUsername(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getAccessRole().toString()));
        this.user = user;
    }

    public Long getId() {
        return user.getId();
    }

    public AccessRole getRole() {
        return user.getAccessRole();
    }
}
