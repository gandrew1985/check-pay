package com.andrzej.payroll.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppUserDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}
