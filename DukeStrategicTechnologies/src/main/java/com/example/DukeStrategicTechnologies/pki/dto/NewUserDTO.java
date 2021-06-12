package com.example.DukeStrategicTechnologies.pki.dto;

public class NewUserDTO {
    private Long id;
    private String name;
    private String surname;
    private String commonName;
    private String organization;
    private String organizationUnit;
    private String state;
    private String city;
    private String email;
    private String password;
    private String confirmPassword;
    private boolean isCA;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    private Long certificateCount;

    public NewUserDTO() {
    }

    public NewUserDTO(Long id, String givenName, String surname, String commonName, String organization, String organizationUnit, String state, String city, String email, String password, String confirmPassword, boolean isCA, Long certificateCount) {
        this.id = id;
        this.name = givenName;
        this.surname = surname;
        this.commonName = commonName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.state = state;
        this.city = city;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.isCA = isCA;
        this.certificateCount = certificateCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String givenName) {
        this.name = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isCA() {
        return isCA;
    }

    public void setCA(boolean CA) {
        isCA = CA;
    }

    public Long getCertificateCount() {
        return certificateCount;
    }

    public void setCertificateCount(Long certificateCount) {
        this.certificateCount = certificateCount;
    }
}
