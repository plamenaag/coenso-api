package com.primeholding.coenso.controller;

import com.primeholding.coenso.configuration.JsonWebTokenConfiguration;
import com.primeholding.coenso.entity.Account;
import com.primeholding.coenso.model.smtp.EmailModel;
import com.primeholding.coenso.model.smtp.ForgotPasswordModel;
import com.primeholding.coenso.model.smtp.MessageModel;
import com.primeholding.coenso.security.UserCredentials;
import com.primeholding.coenso.security.service.SecurityService;
import com.primeholding.coenso.service.AccountService;
import com.primeholding.coenso.util.CodeGeneratorUtil;
import com.primeholding.coenso.util.Mailer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Api("Endpoint for authentication")
public class AuthenticationController {

    public static final String ERROR = "Wrong reset code!";
    private SecurityService securityService;
    private JsonWebTokenConfiguration configuration;
    private Mailer mailer;
    @Value("${primeholding.rushhour.mail}")
    private String fromEmail;
    private PasswordEncoder encoder;
    private AccountService accountService;

    @Autowired
    public AuthenticationController(SecurityService securityService,
                                    JsonWebTokenConfiguration configuration) {
        this.securityService = securityService;
        this.configuration = configuration;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setMailer(Mailer mailer) {
        this.mailer = mailer;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Error in credentials")
    })
    public HttpEntity authenticate(@Valid @RequestBody UserCredentials userCredentials, HttpServletResponse response) {
        String jsonWebToken = securityService.authenticate(userCredentials);
        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader(configuration.getHeader(), jsonWebToken);

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping("/forgot-password")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Email must not be null; Email must be a well-formed email address"),
            @ApiResponse(code = 404, message = "Account not found")
    })
    public ResponseEntity<Object> forgotPassword(@Valid @RequestBody EmailModel emailModel) {
        String toEmail = emailModel.getEmail();
        Optional<Account> account = accountService.get(toEmail);
        if (account.isPresent()) {
            String code = new CodeGeneratorUtil().generateCode();
            if (mailer.sendEmail(fromEmail, toEmail, code, "Password reset code")) {
                account.get().setResetCode(code);
                accountService.update(account.get());
                return ResponseEntity.ok(new MessageModel("Check your email for the reset code we sent"));
            }
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/reset-password")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Email must not be null; Email must be a well-formed email address; " +
                    "ResetCode must not be blank; ResetCode size must be between 4 and 50; " +
                    "Password must not be blank; Password size must be between 2 and 255; " +
                    ERROR),
            @ApiResponse(code = 404, message = "Account not found")
    })
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ForgotPasswordModel resetData) {
        Optional<Account> account = accountService.get(resetData.getEmail());
        if (account.isPresent()) {
            if (account.get().getResetCode() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageModel(ERROR));
            }
            if (!account.get().getResetCode().equals(resetData.getResetCode())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageModel(ERROR));
            }
            account.get().setResetCode(null);
            account.get().setPassword(this.encoder.encode(resetData.getNewPassword()));
            accountService.update(account.get());
            return ResponseEntity.ok(new MessageModel("Password changed successfully!"));
        }
        return ResponseEntity.notFound().build();
    }
}