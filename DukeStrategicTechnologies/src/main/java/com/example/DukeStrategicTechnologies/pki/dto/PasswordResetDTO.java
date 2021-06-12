package com.example.DukeStrategicTechnologies.pki.dto;

public class PasswordResetDTO {
    private String email;
    private String password;
    private String confirmedpassword;
    private String code;

    public PasswordResetDTO(String email, String password, String confirmedpassword, String code) {
        this.email = email;
        this.password = password;
        this.confirmedpassword = confirmedpassword;
        this.code = code;
    }

    public PasswordResetDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmedpassword() {
        return confirmedpassword;
    }

    public void setConfirmedpassword(String confirmedpassword) {
        this.confirmedpassword = confirmedpassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
