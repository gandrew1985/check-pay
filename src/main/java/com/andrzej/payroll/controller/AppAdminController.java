package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.exception.NotFoundException;
import com.andrzej.payroll.mapper.UserMapper;
import com.andrzej.payroll.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/users")
public class AppAdminController {

    /*
     * REVIEW
     * 1. Użyj Lomboka zeby usunąć jawny konstruktor -> szczegóły w komentarzu w UserDbService
     * 2. Masz w tej klasie wymieszaną warstwe Controllera z Repository. Wywoływania metod z Repository powinny sie znaleść w jakimś Servisie jako klasie pośredniej.
     * 3. Niepoprawnie nazywasz urle. Zamiast '/createUser' powinno być '/users'. To że jest to POST wskazuje już że to słuzy to tworzenia usera. To samo '/editUser' -> '/users'.
     * Jak poprawisz wsyztskie metody to okaże się że wszystkie metody mają część wspolną '/users' i to bedziesz mógł przenieść do adnotacji @RequestMapping("/users") nad klasą.
     */

    private UserMapper userMapper;
    private AppUserRepository appUserRepository;

    @Autowired
    public AppAdminController(UserMapper userMapper, AppUserRepository appUserRepository) {
        this.userMapper = userMapper;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/createUser")
    public void createUser(@RequestBody AppUserDto appUserDto) {
        appUserRepository.save(userMapper.mapToAppUser(appUserDto));
    }

    @PutMapping("/editUser")
    public AppUserDto editUser(@RequestBody AppUserDto appUserDto) {
        return userMapper.mapToAppUserDto(appUserRepository.save(userMapper.mapToAppUser(appUserDto)));
    }

    @GetMapping("/getUser/{id}")
    public AppUserDto getUser(@PathVariable Long id) throws NotFoundException {
        return userMapper.mapToAppUserDto(appUserRepository.findById(id).orElseThrow(() ->
                new NotFoundException("User does not exist")));
    }

    @GetMapping("/getAllUsers")
    public List<AppUserDto> getAll() {

        return userMapper.mapToAppUserDtoList(appUserRepository.findAll());
    }

    @DeleteMapping("/deleteUser/{id}")
    public void delete(@PathVariable Long id) {
        appUserRepository.deleteById(id);
    }
}
