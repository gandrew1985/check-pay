package com.andrzej.payroll.core.security;

import com.andrzej.payroll.core.exception.UserAlreadyExistException;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {

    private final AppUserService appUserService;

    @Override
    public void register(AppUser appUser) throws UserAlreadyExistException {
        if (checkIfUserExistByUsername(appUser.getUsername())) {
            throw new UserAlreadyExistException("User for that username already exist");
        } else if (checkIfUserExistByEmail(appUser.getEmail())) {
            throw new UserAlreadyExistException("User for that email already exist");
        }
        appUserService.addUser(appUser);
    }
 /*REVIEW
 * to poniżej da sie zapisać prościej appUserService.findUserByEmail(email) != null; :)
 * */
    @Override
    public boolean checkIfUserExistByEmail(String email) {
        return appUserService.findUserByEmail(email) != null ? true : false;
    }

    @Override
    public boolean checkIfUserExistByUsername(String username) {
        return appUserService.findUserByUsername(username) != null ? true : false;
    }
}
