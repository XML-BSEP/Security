package com.example.DukeStrategicTechnologies.pki.repository;

import com.example.DukeStrategicTechnologies.pki.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

}
