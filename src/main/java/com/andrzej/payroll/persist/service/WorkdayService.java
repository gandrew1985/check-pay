package com.andrzej.payroll.persist.service;

import com.andrzej.payroll.core.exception.NotFoundException;
import com.andrzej.payroll.persist.entity.AppUser;
import com.andrzej.payroll.persist.entity.Workday;
import com.andrzej.payroll.persist.repository.WorkdayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class WorkdayService {

    private final WorkdayRepository workdayRepository;

    public Workday findWorkdayById(Long id) throws NotFoundException {
        return workdayRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workday not found"));
    }

    public List<Workday> getAllWorkdays() {
        return workdayRepository.findAll();
    }

    public List<Workday> findAllByAppUser(AppUser appUser, int pageNumber, int rowPerPage) {
        List<Workday> workdaysList = new ArrayList<>();
        Pageable sortByDateAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("date").ascending());
        workdayRepository.findAllByAppUser(appUser, sortByDateAsc).forEach(workdaysList::add);
        return workdaysList;
    }

    public Workday getWorkdayByDateAndUser(LocalDate date, AppUser appUser) {
        return workdayRepository.findByDateAndAppUser(date, appUser);
    }

    public boolean existWorkdayByDateAndUser(LocalDate date, AppUser appUser) {
        return workdayRepository.existsByDateAndAppUser(date, appUser);
    }

    public Workday updateWorkday(Workday workday) {
        Workday editedWorkday = workdayRepository.findById(workday.getId())
                .orElseThrow(() -> new NotFoundException("Workday does not exist"));
        editedWorkday.setStartTime(workday.getStartTime());
        editedWorkday.setFinishTime(workday.getFinishTime());
        editedWorkday.setDeductionTime(workday.getDeductionTime());
        editedWorkday.setTotalPayableTime(workday.getTotalPayableTime());
        editedWorkday.setBeforeTaxIncome(workday.getBeforeTaxIncome());
        editedWorkday.setAfterTaxIncome(workday.getAfterTaxIncome());
        return editedWorkday;
    }

    public void deleteWorkday(Long id) {
        workdayRepository.deleteById(id);
    }

    public void saveWorkday(Workday workday) {
        workdayRepository.save(workday);
    }

    public Long count() {
        return workdayRepository.count();
    }
}
