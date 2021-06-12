package com.example.DukeStrategicTechnologies.pki.dto;

public class UserTemplateDTO {
    private Long userId;

    public UserTemplateDTO(Long userId) {
        this.userId = userId;
    }

    public UserTemplateDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
