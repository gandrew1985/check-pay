package com.andrzej.payroll.core.security;

import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.core.exception.UserAlreadyExistException;

public interface UserService {

    void register(AppUser appUser) throws UserAlreadyExistException;

    boolean checkIfUserExistByEmail(String email);

    boolean checkIfUserExistByUsername(String username);
}
