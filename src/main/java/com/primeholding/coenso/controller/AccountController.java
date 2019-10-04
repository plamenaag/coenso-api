package com.primeholding.coenso.controller;

import com.primeholding.coenso.entity.Account;
import com.primeholding.coenso.model.AccountPostModel;
import com.primeholding.coenso.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
@Api("Endpoint for creating an account")
public class AccountController {

    private AccountService accountService;
    private ModelMapper modelMapper;

    @Autowired
    public AccountController(AccountService accountService, ModelMapper modelMapper) {
        this.accountService = accountService;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Invalid email format")
    })
    public HttpEntity create(@Valid @RequestBody AccountPostModel postModel) {
        Account account = modelMapper.map(postModel, Account.class);

        Optional<Account> optionalAccount = accountService.create(account);

        return optionalAccount.map(a ->
                ResponseEntity.created(URI.create("/api/v1/accounts/" + a.getId()))
                        .build())
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT)
                        .build());
    }
}
