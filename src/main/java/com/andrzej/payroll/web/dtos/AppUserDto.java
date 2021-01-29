package com.andrzej.payroll.web.dtos;

import com.andrzej.payroll.persist.entity.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;

    @Size(max = 100)
    @NotBlank(message = "username")
    private String username;
    @NotEmpty
    @Size(min = 3, max = 15, message = "Password can`t be shorter than 3 char, and greater than 15")
    private String password;
    private String matchingPassword;
    @NotEmpty
    @Email(regexp = ".+@.+\\..+")
    private String email;
    private String role;
    private Rate rate;
}
