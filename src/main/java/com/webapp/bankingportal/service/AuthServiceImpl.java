package com.webapp.bankingportal.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.webapp.bankingportal.dto.ResetPasswordRequest;
import com.webapp.bankingportal.entity.PasswordResetToken;
import com.webapp.bankingportal.entity.User;
import com.webapp.bankingportal.repository.PasswordResetTokenRepository;
import com.webapp.bankingportal.util.ApiMessages;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int EXPIRATION_HOURS = 24;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;

    @Override
    public String generatePasswordResetToken(User user) {
        val existingToken = passwordResetTokenRepository.findByUser(user);
        if (isExistingTokenValid(existingToken)) {
            return existingToken.getToken();
        }

        val token = UUID.randomUUID().toString();
        val expiryDateTime = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        val resetToken = new PasswordResetToken(token, user, expiryDateTime);
        passwordResetTokenRepository.save(resetToken);

        return token;
    }

    @Override
    public boolean verifyPasswordResetToken(String token, User user) {
        return passwordResetTokenRepository.findByToken(token)
                .map(resetToken -> {
                    deletePasswordResetToken(token);
                    return user.equals(resetToken.getUser()) && resetToken.isTokenValid();
                })
                .orElse(false);
    }

    @Override
    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public ResponseEntity<String> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        val user = userService.getUserByIdentifier(resetPasswordRequest.identifier());

        if (!verifyPasswordResetToken(resetPasswordRequest.resetToken(), user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiMessages.TOKEN_INVALID_ERROR.getMessage());
        }

        try {
            boolean passwordResetSuccessful = userService.resetPassword(user, resetPasswordRequest.newPassword());
            if (passwordResetSuccessful) {
                return ResponseEntity.ok(ApiMessages.PASSWORD_RESET_SUCCESS.getMessage());
            } else {
                return ResponseEntity.internalServerError().body(ApiMessages.PASSWORD_RESET_FAILURE.getMessage());
            }
        } catch (Exception e) {
            log.error("Error resetting password for user: {}", user.getId(), e);
            return ResponseEntity.internalServerError().body(ApiMessages.PASSWORD_RESET_FAILURE.getMessage());
        }
    }

    private boolean isExistingTokenValid(PasswordResetToken existingToken) {
        return existingToken != null && existingToken.getExpiryDateTime().isAfter(LocalDateTime.now().plusMinutes(5));
    }

}
