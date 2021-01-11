package com.andrzej.payroll.domain;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    /* @NoArgConstructor uzywam  w Controllerach, np.
    @GetMapping("/getWorkdays")
    public String showAll(Model model) {
        AppUser loggedUser = timeService.getLoggedUser();
        List<WorkdayDto> allWorkdaysDto = serviceMapper.mapToWorkdayDtoList(dbService.findAllByAppUser(loggedUser));
        model.addAttribute("allDaysListDto", allWorkdaysDto);
        model.addAttribute("workdayDto", new WorkdayDto());
        return "allDays";
     */
    /* MAREK
    * OK.
    * Tak czy naczej staraj sie utrzymywać jak najmnijesza ilość konstruktorów. Jezeli jest ich dużo -> wzorzec Builder albo przemyśl logike.
    * Dąż do tego żeby każda klasa mogła by tworzona tylko jedynm kostruktorem choć wiem ze niektórymi frameworkami jest o to cięzko wiec to sie nie zawsze uda.
     */

    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Rate rate;
}
