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

    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Rate rate;
}
