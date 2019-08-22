package com.automatyka.pbd.security;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AppUser {
    long id;
    String username;
    String passwordHash;
    AccessRole accessRole;
    ProfileType profileType;
}
