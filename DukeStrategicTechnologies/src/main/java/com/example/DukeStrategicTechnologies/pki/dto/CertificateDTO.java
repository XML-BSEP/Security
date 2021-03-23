package com.example.DukeStrategicTechnologies.pki.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

public class CertificateDTO {

    private String serialNumber;
    private Date startDate;
    private Date endDate;
    private String signatureAlgorithm;
    private Collection<String> keyUsages;
    private Collection<String> extendedKeyUsages;

    public CertificateDTO(String serialNumber, Date startDate,
                          Date endDate, String signatureAlgorithm,
                          Collection<String> keyUsages, Collection<String> extendedKeyUsages) {
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsages = keyUsages;
        this.extendedKeyUsages = extendedKeyUsages;
    }

    public CertificateDTO() {
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
