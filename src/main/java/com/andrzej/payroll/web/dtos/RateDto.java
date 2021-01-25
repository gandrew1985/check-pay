package com.andrzej.payroll.web.dtos;

import com.andrzej.payroll.persist.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {

    private Long id;
    @NotNull
    @PositiveOrZero
    private double hourlyRate;
    @NotNull
    @PositiveOrZero
    private double overtimeRate;
    @NotNull
    @PositiveOrZero
    private double nightRate;
    @NotNull
    @PositiveOrZero
    private double locationRate;
    @NotNull
    @PositiveOrZero
    private double allowance;
    @NotNull
    @PositiveOrZero
    private double pensionScheme;
    private AppUser appUser;

}
