package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.account.AccountDTO;
import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.mapper.AccountMapper;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.entities.User;
import com.example.health_management.domain.repositories.AccountRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    public void updateAccount(UpdateAccountRequest updateAccountRequest, @NonNull User user) {
        Account account = accountMapper.updateFromDTO(updateAccountRequest, user.getAccount());
        user.setAccount(account);
    }
}
