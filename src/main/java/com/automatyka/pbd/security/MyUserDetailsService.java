package com.automatyka.pbd.security;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        val appUser = userService.findByUsername(s);
        if (appUser == null) throw new UsernameNotFoundException(String.format("User with name: %s already exists", s));
        return new CurrentUser(appUser);
    }
}
