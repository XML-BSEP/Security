package com.example.DukeStrategicTechnologies.pki.model;

import com.example.DukeStrategicTechnologies.pki.model.enums.KeyUsage;
import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

public class ExtendedCertificateData {

    private LocalDate startDate;
    private LocalDate endDate;
    private SignatureAlgorithm signatureAlgorithm;
    private Collection<Integer> keyUsage;
    private BigInteger serialNumber;
    private KeyUsage selectedKeyUsage;

    public ExtendedCertificateData() {
    }

    public ExtendedCertificateData(LocalDate startDate, LocalDate endDate, SignatureAlgorithm signatureAlgorithm,
                                   Collection<Integer> keyUsage, BigInteger serialNumber, KeyUsage selectedKeyUsage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsage = keyUsage;
        this.serialNumber = serialNumber;
        this.selectedKeyUsage = selectedKeyUsage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Collection<Integer> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(Collection<Integer> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public KeyUsage getSelectedKeyUsage() {
        return selectedKeyUsage;
    }

    public void setSelectedKeyUsage(KeyUsage selectedKeyUsage) {
        this.selectedKeyUsage = selectedKeyUsage;
    }
}
