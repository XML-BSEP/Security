package com.example.DukeStrategicTechnologies.pki.repository;

import com.example.DukeStrategicTechnologies.pki.model.RevokedCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedCertificateRepository extends JpaRepository<RevokedCertificate, Long> {
    RevokedCertificate findBySerialNumber(String serialNumber);
}
