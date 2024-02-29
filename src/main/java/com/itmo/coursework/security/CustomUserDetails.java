package com.itmo.coursework.security;

import com.itmo.coursework.model.User;
import com.itmo.coursework.repository.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final UserRoleRepository userRoleRepository;


    public CustomUserDetails(User user, UserRoleRepository userRoleRepository) {
        this.user = user;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return StreamSupport.stream(userRoleRepository.findUserRoleByUser(user).spliterator(), false)
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
