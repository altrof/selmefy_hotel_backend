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
import org.thymeleaf.context.Context;
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
import static tech.selmefy.hotel.validators.ObjectUtilityValidator.isNullOrEmpty;

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

        // Username is cant be null
        if (isNullOrEmpty(signupRequest.getUsername())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Username is required.");
        }

        // Password is cant be null
        if (isNullOrEmpty(signupRequest.getPassword())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Password is required.");
        }

        // Identity code is cant be null
        if (isNullOrEmpty(signupRequest.getIdentityCode())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Identity code is required.");
        }

        // Firstname is cant be null
        if (isNullOrEmpty(signupRequest.getFirstName())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Firstname is required.");
        }

        // Lastname is cant be null
        if (isNullOrEmpty(signupRequest.getLastName())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Lastname is required.");
        }

        // Country is cant be null
        if (isNullOrEmpty(signupRequest.getCountry())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Country is required.");
        }

        // Phone number is cant be null
        if (isNullOrEmpty(signupRequest.getPhoneNumber())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Phone number is required.");
        }

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

        // Email validation with regex pattern
        if (!emailValidator.test(signupRequest.getEmail())) {
            return ResponseEntity
                .badRequest()
                .body("Error: Email is not valid");
        }

        // Validation for date of birt. 18+
        if (Period.between(signupRequest.getDateOfBirth(), LocalDate.now()).getYears() < 18) {
            return ResponseEntity
                .badRequest()
                .body("Error: You are less than 18 years old.");
        }

        // User length should be not less than 6 chars
        if (signupRequest.getUsername().length() < 6) {
            return ResponseEntity
                .badRequest()
                .body("Error: Username minimum length is 6.");
        }

        // User length should be not less than 6 chars
        if (signupRequest.getPassword().length() < 6) {
            return ResponseEntity
                .badRequest()
                .body("Error: Username minimum length is 6.");
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

        String link = appUtil.getClientBaseUrl() + "/confirm?token=" + token;

        final Context ctx = new Context();
        ctx.setVariable("name", signupRequest.getFirstName());
        ctx.setVariable("confirmationUrl", link);

        emailSender.sendHtmlMessage(
            signupRequest.getEmail(),
            "/html/email_confirm.html",
            ctx
        );

        return ResponseEntity.ok(token);
    }

}
