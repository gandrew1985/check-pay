package com.andrzej.payroll.mapper;

import com.andrzej.payroll.configuration.WebSecurityConfig;
import com.andrzej.payroll.domain.AppUser;
import com.andrzej.payroll.domain.AppUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final WebSecurityConfig securityConfig;

    public AppUser mapToAppUser(AppUserDto appUserDto) {
        return AppUser.builder()
                .id(appUserDto.getId())
                .username(appUserDto.getUsername())
                .password(appUserDto.getPassword())
                .email(appUserDto.getEmail())
                .role(appUserDto.getRole())
                .rate(appUserDto.getRate())
                .build();
    }

    public AppUserDto mapToAppUserDto(AppUser appUser) {
        return AppUserDto.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .password(securityConfig.passwordEncoder().encode(appUser.getPassword()))
                .email(appUser.getEmail())
                .role(appUser.getRole())
                .rate(appUser.getRate())
                .build();
    }

    public List<AppUserDto> mapToAppUserDtoList(List<AppUser> appUserList) {
        return appUserList.stream()
                .map(appUser -> AppUserDto.builder()
                        .id(appUser.getId())
                        .username(appUser.getUsername())
                        .password(securityConfig.passwordEncoder().encode(appUser.getPassword()))
                        .email(appUser.getEmail())
                        .role(appUser.getRole())
                        .rate(appUser.getRate())
                        .build())
                .collect(Collectors.toList());
    }
}
