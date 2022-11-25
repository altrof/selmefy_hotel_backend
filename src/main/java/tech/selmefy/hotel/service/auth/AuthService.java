package tech.selmefy.hotel.service.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.selmefy.hotel.controller.auth.dto.request.LoginRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.request.SignupRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.response.JwtResponseDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.repository.auth.EmailConfirmationToken;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRoleRepository;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;
import tech.selmefy.hotel.repository.user_account.entity.UserAccountRole;
import tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType;
import tech.selmefy.hotel.security.jwt.JwtTokenUtil;
import tech.selmefy.hotel.security.services.JwtUserDetails;
import tech.selmefy.hotel.utils.AppUtil;
import tech.selmefy.hotel.utils.email_sender.EmailSender;
import tech.selmefy.hotel.validators.EmailValidator;
import tech.selmefy.hotel.service.person.PersonService;
import tech.selmefy.hotel.service.user_account.UserAccountService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountRoleRepository userAccountRoleRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    public final PersonService personService;
    public final PersonRepository personRepository;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailValidator emailValidator;
    private final UserAccountService userAccountService;
    private final EmailSender emailSender;

    private final AppUtil appUtil;

    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtTokenUtil.generateJwtToken(authentication);

        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        List<String> userRoles = jwtUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .toList();

        return new JwtResponseDTO(
            jwtToken,
            jwtUserDetails.getUsername(),
            jwtUserDetails.getEmail(),
            userRoles
        );
    }

    @Transactional
    public ResponseEntity<Object> confirmToken(String token) {
        EmailConfirmationToken confirmationToken = emailConfirmationTokenService
            .getToken(token)
            .orElseThrow(() -> new IllegalStateException("Token not found!"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email is already confirmed!");
        }

        LocalDateTime expiresAt = confirmationToken.getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token is expired");
        }

        emailConfirmationTokenService.setConfirmedAt(token);
        userAccountService.enableUserAccount(confirmationToken.getUserAccount().getEmail());

        return ResponseEntity.ok("Account is confirmed.");
    }

    public ResponseEntity<Object> registerUser(SignupRequestDTO signupRequest) {
        if (userAccountRepository.existsByIdentityCode(signupRequest.getIdentityCode())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Account with this identity code is already exists.");
        }

        if (userAccountRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Username is already taken!");
        }

        if (userAccountRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Email is already in use!");
        }

        if (!emailValidator.test(signupRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Email is not valid");
        }

        if(Period.between(signupRequest.getDateOfBirth(), LocalDate.now()).getYears() < 18) {
            return ResponseEntity
                .badRequest()
                .body("Error: You are less than 18 years old.");
        }

        Long personId;

        if(personRepository.existsPersonByIdentityCode(signupRequest.getIdentityCode())) {
            personId = personRepository.findPersonByIdentityCode(signupRequest.getIdentityCode())
                .orElseThrow(() -> new RuntimeException("Error: No person with provided identity code.")).getId();
        } else {
            PersonDTO personDTO = new PersonDTO(
                signupRequest.getIdentityCode(),
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getDateOfBirth(),
                signupRequest.getCountry(),
                signupRequest.getPhoneNumber()
                );
            personId = personService.createNewPerson(personDTO);
        }



        UserAccount userAccount = new UserAccount(
            personId,
            signupRequest.getUsername(),
            passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getEmail(),
            signupRequest.getIdentityCode()
        );

        Set<String> strRoles = signupRequest.getRole();
        Set<UserAccountRole> userAccountRoles = new HashSet<>();

        if (strRoles == null) {
            UserAccountRole userAccountRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_DEFAULT_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userAccountRoles.add(userAccountRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "superuser" -> {
                        UserAccountRole adminRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_SUPERUSER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userAccountRoles.add(adminRole);
                    }
                    case "admin" -> {
                        UserAccountRole modRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userAccountRoles.add(modRole);
                    }
                    default -> {
                        UserAccountRole userRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_DEFAULT_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userAccountRoles.add(userRole);
                    }
                }
            });
        }

        userAccount.setRoles(userAccountRoles);
        userAccountRepository.save(userAccount);

        String token = UUID.randomUUID().toString();
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            userAccount
        );

        emailConfirmationTokenService.saveEmailConfirmationToken(confirmationToken);

        System.out.println("URL: " + appUtil.getClientBaseUrl());
        String link = appUtil.getClientBaseUrl() + "/confirm?token=" + token;
        emailSender.send(
            signupRequest.getEmail(),
            buildEmailMessage(signupRequest.getFirstName(), link)
        );

        return ResponseEntity.ok(token);
    }

    private String buildEmailMessage(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
            "\n" +
            "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
            "\n" +
            "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "    <tbody><tr>\n" +
            "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
            "        \n" +
            "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
            "          <tbody><tr>\n" +
            "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
            "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
            "                  <tbody><tr>\n" +
            "                    <td style=\"padding-left:10px\">\n" +
            "                  \n" +
            "                    </td>\n" +
            "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
            "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "              </a>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "        </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
            "    <tbody><tr>\n" +
            "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
            "      <td>\n" +
            "        \n" +
            "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
            "                  <tbody><tr>\n" +
            "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
            "                  </tr>\n" +
            "                </tbody></table>\n" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
            "    </tr>\n" +
            "  </tbody></table>\n" +
            "\n" +
            "\n" +
            "\n" +
            "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
            "    <tbody><tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
            "        \n" +
            "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
            "        \n" +
            "      </td>\n" +
            "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "      <td height=\"30\"><br></td>\n" +
            "    </tr>\n" +
            "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
            "\n" +
            "</div></div>";
    }

}
