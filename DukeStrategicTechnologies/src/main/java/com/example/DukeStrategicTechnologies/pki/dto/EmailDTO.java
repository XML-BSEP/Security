package com.example.DukeStrategicTechnologies.pki.dto;

public class EmailDTO {
    private String email;
    private String subject;
    private String body;
    private String verificationCode;

    public EmailDTO() {
    }

    public EmailDTO(String email, String subject, String body, String verificationCode) {
        this.email = email;
        this.subject = subject;
        this.body = body;
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String patientEmail) {
        this.email = patientEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}