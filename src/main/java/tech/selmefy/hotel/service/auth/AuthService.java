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
import tech.selmefy.hotel.controller.auth.dto.request.LoginRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.request.SignupRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.response.JwtResponseDTO;
import tech.selmefy.hotel.controller.person.dto.PersonDTO;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRoleRepository;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;
import tech.selmefy.hotel.repository.user_account.entity.UserAccountRole;
import tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType;
import tech.selmefy.hotel.security.jwt.JwtTokenUtil;
import tech.selmefy.hotel.security.services.JwtUserDetails;
import tech.selmefy.hotel.service.person.PersonService;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public ResponseEntity<?> registerUser(SignupRequestDTO signupRequest) {
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
                signupRequest.getDateOfBirth()
            );
            personId = personService.createNewPerson(personDTO);
        }



        UserAccount userAccount = new UserAccount(
            personId,
            signupRequest.getUsername(),
            signupRequest.getEmail(),
            passwordEncoder.encode(signupRequest.getPassword()),
            signupRequest.getFirstName(),
            signupRequest.getLastName(),
            signupRequest.getCountry(),
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
                    case "superuser":
                        UserAccountRole adminRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_SUPERUSER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userAccountRoles.add(adminRole);

                        break;
                    case "admin":
                        UserAccountRole modRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userAccountRoles.add(modRole);

                        break;
                    default:
                        UserAccountRole userRole = userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_DEFAULT_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        userAccountRoles.add(userRole);
                }
            });
        }

        userAccount.setRoles(userAccountRoles);
        userAccountRepository.save(userAccount);

        return ResponseEntity.ok("Account registered successfully!");
    }
}
