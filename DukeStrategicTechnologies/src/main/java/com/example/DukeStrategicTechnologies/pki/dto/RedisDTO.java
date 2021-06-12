package com.example.DukeStrategicTechnologies.pki.dto;

import com.example.DukeStrategicTechnologies.pki.model.Account;
import com.example.DukeStrategicTechnologies.pki.model.User;

public class RedisDTO {
    private Account account;
   private User user;
    private String code;
    public RedisDTO(Account account, User user, String verificationCode) {
        this.account = account;
        this.user = user;
        this.code = verificationCode;
    }

    public RedisDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
