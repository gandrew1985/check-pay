package com.andrzej.payroll.persist.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double hourlyRate;
    private double overtimeRate;
    private double nightRate;
    private double locationRate;
    private double allowance;
    private double pensionScheme;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;

    @Override
    public String toString() {
        return "Rate{" +
                "id=" + id +
                ", hourlyRate=" + hourlyRate +
                ", overtimeRate=" + overtimeRate +
                '}';
    }
}

