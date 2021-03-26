package com.example.DukeStrategicTechnologies.pki.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String givenName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "common_name")
    private String commonName;
    
    @Column(name = "organization")
    private String organization;

    @Column(name = "organization_unit")
    private String organizationUnit;
    
    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "email")
    private String email;

    @Column(name = "isCA")
    private boolean isCA;

    @Column(name = "certificate_count")
    private Long certificateCount;

    public User() {
    }

    public User(Long id, String givenName, String surname, String commonName,
                String organization, String organizationUnit, String state, String city,
                String email, boolean isCA, Long certificateCount) {
        this.id = id;
        this.givenName = givenName;
        this.surname = surname;
        this.commonName = commonName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.state = state;
        this.city = city;
        this.email = email;
        this.isCA = isCA;
        this.certificateCount = certificateCount;
    }

    public User(String givenName, String surname, String commonName, String organization, String organizationUnit,
                String state, String city, String email, boolean isCA, Long certificateCount) {
        this.givenName = givenName;
        this.surname = surname;
        this.commonName = commonName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.state = state;
        this.city = city;
        this.email = email;
        this.isCA = isCA;
        this.certificateCount = certificateCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
}
