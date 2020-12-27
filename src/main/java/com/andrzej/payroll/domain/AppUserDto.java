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

    /*
     * REVIEW
     * Po co Ci tu @NoArgsConstructor? Nigdzie tego chyba nie używasz.
     * Jakbyś oznaczył poniższe pola jako finalne, to wszyskie adnotacje nad klasą móglbyś zastąpić jedną @Data i wszystko bedzie dzialać.
     */


    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
}
