package com.automatyka.pbd.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Qualifier("userDetailsService")
    private final MyUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/calendar/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/events/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/results/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/athletes/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/me/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/search/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .antMatchers("/training_plans/**").access("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .rememberMe();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
