package com.andrzej.payroll.core.security;

import com.andrzej.payroll.core.exception.NotFoundException;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public AppUser getLoggedUser() {
        Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return (AppUser) object;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
    }


}
