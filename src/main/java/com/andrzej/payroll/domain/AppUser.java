package com.andrzej.payroll.domain;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "USERS")
public class AppUser implements UserDetails {

    /* @Column uzywam po to zeby zastosowac Constraints y do poszczegolnych kolumn...
     * probowalem importowac javax.validation.constraints, dodalem zaleznosc do gradle.build
     * ale nie dzialalo(moze dlatego ze dodalem najnowsza wersje z maven repository?)
     * dlaczego nie moge w spring.init ustawic wyzszej Javy niz 8? wyskakuje ze SDK tylko dziala z 8...??
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,unique = true,length = 100)
    private String username;
    @Column(nullable = false,unique = true,length = 100)
    private String password;
    @Column(nullable = false,unique = true,length = 100)
    private String email;
    private String role;

    @OneToOne(mappedBy = "appUser")
    private Rate rate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
