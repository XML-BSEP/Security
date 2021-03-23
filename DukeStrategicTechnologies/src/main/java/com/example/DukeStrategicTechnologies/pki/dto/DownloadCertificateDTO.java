package com.example.DukeStrategicTechnologies.pki.dto;

public class DownloadCertificateDTO {
    private String subjectEmail;
    private String serialNumber;
    private String commonName;

    public DownloadCertificateDTO() {
    }

    public DownloadCertificateDTO(String subjectEmail, String serialNumber, String commonName) {
        this.subjectEmail = subjectEmail;
        this.serialNumber = serialNumber;
        this.commonName = commonName;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
}
