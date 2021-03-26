package com.example.DukeStrategicTechnologies.pki.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class CertificateDTO {

    private String serialNumber;
    private Date startDate;
    private Date endDate;
    private String commonName;
    private String email;
    private String signatureAlgorithm;
    private Collection<String> keyUsages;
    private Collection<String> extendedKeyUsages;
    private boolean isRevoked;
    private Long issuerId;


    public CertificateDTO(String serialNumber, Date startDate, Date endDate,
                          String commonName, String email, String signatureAlgorithm,
                          Collection<String> keyUsages, Collection<String> extendedKeyUsages, boolean isRevoked, Long issuerId) {
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.issuerId = issuerId;
        this.endDate = endDate;
        this.commonName = commonName;
        this.email = email;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsages = keyUsages;
        this.extendedKeyUsages = extendedKeyUsages;
        this.isRevoked = isRevoked;
    }

    public boolean isRevoked() {
        return isRevoked;
    }

    public void setRevoked(boolean revoked) {
        isRevoked = revoked;
    }

    public CertificateDTO() {
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Collection<String> getKeyUsages() {
        return keyUsages;
    }

    public void setKeyUsages(Collection<String> keyUsages) {
        this.keyUsages = keyUsages;
    }

    public Collection<String> getExtendedKeyUsages() {
        return extendedKeyUsages;
    }

    public void setExtendedKeyUsages(Collection<String> extendedKeyUsages) {
        this.extendedKeyUsages = extendedKeyUsages;
    }
}
