package com.example.DukeStrategicTechnologies.pki.dto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

public class TemplateDTO {

    private Long id;
    private String signatureAlgorithm;
    private String keyAlgorithm;
    private String name;
    private LocalDateTime timestamp;
    private Set<String> keyUsage;
    private Set<String> extendedKeyUsage;


    public TemplateDTO(Long id, String signatureAlgorithm, String keyAlgorithm, String name,
                       LocalDateTime timestamp, Set<String> keyUsage, Set<String> extendedKeyUsage) {
        this.id = id;
        this.signatureAlgorithm = signatureAlgorithm;
        this.keyAlgorithm = keyAlgorithm;
        this.name = name;
        this.timestamp = timestamp;
        this.keyUsage = keyUsage;
        this.extendedKeyUsage = extendedKeyUsage;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public void setKeyAlgorithm(String keyAlgorithm) {
        this.keyAlgorithm = keyAlgorithm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Set<String> getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(Set<String> keyUsage) {
        this.keyUsage = keyUsage;
    }

    public Set<String> getExtendedKeyUsage() {
        return extendedKeyUsage;
    }

    public void setExtendedKeyUsage(Set<String> extendedKeyUsage) {
        this.extendedKeyUsage = extendedKeyUsage;
    }
}
