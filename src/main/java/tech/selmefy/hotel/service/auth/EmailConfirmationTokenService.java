package tech.selmefy.hotel.service.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.repository.auth.EmailConfirmationToken;
import tech.selmefy.hotel.repository.auth.EmailConfirmationTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmailConfirmationTokenService {

    public final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    public void saveEmailConfirmationToken(EmailConfirmationToken confirmToken) {
        emailConfirmationTokenRepository.save(confirmToken);
    }

    public Optional<EmailConfirmationToken> getToken(String token) {
        return emailConfirmationTokenRepository.findByConfirmToken(token);
    }

    public int setConfirmedAt(String token) {
        return emailConfirmationTokenRepository.updateConfirmedAt(
            token, LocalDateTime.now());
    }
}
