package com.andrzej.payroll.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private double hourlyRate;
    @Column(nullable = false)
    private double overtimeRate;
    @Column(nullable = false)
    private double nightRate;
    @Column(nullable = false)
    private double locationRate;
    @Column(nullable = false)
    private double pensionScheme;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser appUser;
}

