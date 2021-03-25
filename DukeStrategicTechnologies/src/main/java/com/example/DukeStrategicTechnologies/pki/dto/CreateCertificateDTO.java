package com.example.DukeStrategicTechnologies.pki.dto;

import com.example.DukeStrategicTechnologies.pki.model.enums.SignatureAlgorithm;


import java.time.LocalDate;
import java.util.Collection;

public class CreateCertificateDTO {


    private Long subjectId;
    private Long issuerId;
    private String startDate;
    private String endDate;
    private SignatureAlgorithm signatureAlgorithm;
    private Collection<String> keyUsage;
    private Collection<String> extendedKeyUsage;
    private String issuerSerialNumber;

    public CreateCertificateDTO(Long subjectId, Long issuerId, String startDate, String endDate,
                                SignatureAlgorithm signatureAlgorithm, Collection<String> keyUsage,
                                Collection<String> extendedKeyUsage,
                                String issuerSerialNumber) {
        this.subjectId = subjectId;
        this.issuerId = issuerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public Collection<String> getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(Collection<String> extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(SignatureAlgorithm signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Collection<String> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(Collection<String> keyUsage) {
        this.keyUsage = keyUsage;
    }


    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }
}
