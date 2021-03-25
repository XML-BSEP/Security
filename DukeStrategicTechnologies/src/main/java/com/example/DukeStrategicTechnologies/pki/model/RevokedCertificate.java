package com.example.DukeStrategicTechnologies.pki.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "revoked_certificates")
public class RevokedCertificate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "serial_number")
    private BigInteger serialNumber;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public RevokedCertificate() {
    }

    public RevokedCertificate(BigInteger serialNumber, LocalDateTime timestamp) {
        this.serialNumber = serialNumber;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
