package com.andrzej.payroll.persist.repository;

import com.andrzej.payroll.persist.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    AppUser findAppUserByUsername(String username);

    AppUser findByEmail(String email);
}
