package com.example.DukeStrategicTechnologies.pki.model;

import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;
import org.bouncycastle.asn1.x509.KeyPurposeId;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Collection;

public class ExtendedCertificateData {

    private LocalDate startDate;
    private LocalDate endDate;
    private SignatureAlgorithm signatureAlgorithm;
    private Collection<Integer> keyUsage;
    private KeyPurposeId[] extendedKeyUsages;
    private BigInteger serialNumber;


    public ExtendedCertificateData() {
    }

    public ExtendedCertificateData(LocalDate startDate, LocalDate endDate, SignatureAlgorithm signatureAlgorithm,
                                   Collection<Integer> keyUsage, KeyPurposeId[] extendedKeyUsages,
                                   BigInteger serialNumber) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsage = keyUsage;
        this.extendedKeyUsages = extendedKeyUsages;
        this.serialNumber = serialNumber;
    }

    public KeyPurposeId[] getExtendedKeyUsages() {
        return extendedKeyUsages;
    }

    public void setExtendedKeyUsages(KeyPurposeId[] extendedKeyUsages) {
        this.extendedKeyUsages = extendedKeyUsages;
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

}
