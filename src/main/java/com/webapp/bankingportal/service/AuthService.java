package com.webapp.bankingportal.service;

import org.springframework.http.ResponseEntity;

import com.webapp.bankingportal.dto.ResetPasswordRequest;
import com.webapp.bankingportal.entity.User;

public interface AuthService {
    public String generatePasswordResetToken(User user);

    public boolean verifyPasswordResetToken(String token, User user);

    public void deletePasswordResetToken(String token);

    public ResponseEntity<String> resetPassword(ResetPasswordRequest resetPasswordRequest);
}
