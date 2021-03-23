package com.example.DukeStrategicTechnologies.pki.repository;

import com.example.DukeStrategicTechnologies.pki.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

}
