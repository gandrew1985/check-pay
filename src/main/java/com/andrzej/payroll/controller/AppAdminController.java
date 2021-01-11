package com.andrzej.payroll.controller;

import com.andrzej.payroll.domain.AppUserDto;
import com.andrzej.payroll.mapper.UserMapper;
import com.andrzej.payroll.service.DbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class AppAdminController {

    /* co do nazewnictwa URL to mam troche metlik...
     * bo zeby korzystac z thymeleafa to nazwy URL nie moga sie powtarzac ...
     * wiec za bardzo nie moge wszystkich metod nazwac tak samo i polegac tylko na rodzaju Mappingu
     * a na dodatek thymleaf obsluguje tylko 2 zadania Get i Post...
     * wiec za bardzo nie wiem jak to obejsc....
     * do tego kontrollera tez bede tworzyl front z wykorzystaniem thymeleafa...
     * na bootcampie bylo ze nad klase daje sie np @RequestMapping("/api")
     * a nad metodami to co robia np ("/createUser") ... itp.
     * poprawilem nazwy URL w KOntrollerach ale dalej nie wiem czy dobrze....
     */

    /* MAREK
    * Niewiele robiłem w thymleafie wiec nie jestem najlepszą osobą do podpowiadania.
    * Z drugiej storny jednak API to API i każda strona o dobrych praktykach pisania REST API mówi w
    * zasadzie to samo (mn.in że metody createUser są niewłaściwe) https://phauer.com/2015/restful-api-design-best-practices/
    *
    * Jeżeli faktycznie sie nie mogą powtażać w thymsleafie (upewniłbym sie) to ten przykład jak nazywać urle wygląda
    * całkiem przyzwoicie: https://www.dariawan.com/tutorials/spring/spring-boot-thymeleaf-crud-example/
    * jest praktyka dość powszechna że dodaje sie '/api' i robi sie to po to żeby właśnie wyodrębnić nasze RESTy jako te oficjalne
    * i odróżnić je od tych wygenerowanych (np springowe).
    */

    private final UserMapper userMapper;
    private final DbService dbService;

    @PostMapping("/createUser")
    public void createUser(@RequestBody AppUserDto appUserDto) {
        dbService.addUser(userMapper.mapToAppUser(appUserDto));
    }

    @PutMapping("/updateUser/{id}")
    public AppUserDto editUser(@RequestBody AppUserDto appUserDto) {
        return userMapper.mapToAppUserDto(dbService.editUser(userMapper.mapToAppUser(appUserDto)));
    }

    @GetMapping("/getUser/{id}")
    public AppUserDto getUser(@PathVariable Long id) {
        return userMapper.mapToAppUserDto(dbService.findById(id));
    }

    @GetMapping("/getUsers")
    public List<AppUserDto> getAllUsers() {
        return userMapper.mapToAppUserDtoList(dbService.findAllUsers());
    }

    @DeleteMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        dbService.deleteUser(id);
    }
}
