package com.example.DukeStrategicTechnologies.pki.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "revoked_certificates")
public class RevokedCertificate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "timestamp")
    private Date timestamp;

    public RevokedCertificate() {
    }

    public RevokedCertificate(String serialNumber, Date timestamp) {
        this.serialNumber = serialNumber;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
