package com.example.health_management.domain.cqrs.commands.impl.auth;

import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.Key;

public class RegisterByEmailCommand {
    private Account account;

    public RegisterByEmailCommand(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
