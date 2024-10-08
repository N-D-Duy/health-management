package com.example.health_management.domain.services;

import com.example.health_management.application.DTOs.account.UpdateAccountRequest;
import com.example.health_management.application.guards.JwtProvider;
import com.example.health_management.application.mapper.AccountMapper;
import com.example.health_management.common.shared.enums.Role;
import com.example.health_management.domain.entities.Account;
import com.example.health_management.domain.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    /*
    * update already existing account
    * */
//    public void updateAccount(Long id, UpdateAccountRequest updateAccountRequest) {
//        try {
//            Long currentUserId = jwtProvider.extractUserFromToken().getId();
//
//            if(!currentUserId.equals(id)) {
//                throw new Exception("Unauthorized");
//            }
//            Account account = accountRepository.findById(id).orElseThrow(() -> new Exception("Account not found"));
//            updateAccountRequest.setPassword(passwordEncoder.encode(updateAccountRequest.getPassword()));
//            accountMapper.updateFromDTO(updateAccountRequest, account);
//            accountRepository.save(account);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
