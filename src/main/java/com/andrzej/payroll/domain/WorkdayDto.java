package com.andrzej.payroll.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class WorkdayDto {

    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime finishTime;
    private long deductionTime;

    public WorkdayDto() {
    }

    public WorkdayDto(Long id, LocalDate date, LocalTime startTime,
                      LocalTime finishTime, long deductionTime) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.deductionTime = deductionTime;
    }
}
