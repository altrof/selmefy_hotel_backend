package tech.selmefy.hotel.service.auth;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.selmefy.hotel.controller.auth.dto.request.LoginRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.request.SignupRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.response.JwtResponseDTO;
import tech.selmefy.hotel.repository.auth.EmailConfirmationToken;
import tech.selmefy.hotel.repository.person.Person;
import tech.selmefy.hotel.repository.person.PersonRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRepository;
import tech.selmefy.hotel.repository.user_account.UserAccountRoleRepository;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;
import tech.selmefy.hotel.repository.user_account.entity.UserAccountRole;
import tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType;
import tech.selmefy.hotel.security.jwt.AuthEntryPointJwt;
import tech.selmefy.hotel.security.jwt.AuthTokenFilter;
import tech.selmefy.hotel.security.jwt.JwtTokenUtil;
import tech.selmefy.hotel.security.services.JwtUserDetails;
import tech.selmefy.hotel.service.person.PersonService;
import tech.selmefy.hotel.service.user_account.UserAccountService;
import tech.selmefy.hotel.utils.AppUtil;
import tech.selmefy.hotel.utils.email_sender.EmailSender;
import tech.selmefy.hotel.validators.EmailValidator;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPassword";
    private static final String IDENTITY_CODE = "12345678";
    private static final String EMAIL = "test@email.com";
    private static final String FIRSTNAME = "Testy";
    private static final String LASTNAME = "Testiliano";
    private static final String COUNTRY = "Testland";
    private static final String PHONE_NUMBER = "+37255555555";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 1, 1);
    private static final String EMAIL_TOKEN = "493410b3-dd0b-4b78-97bf-289f50f6e74f";


    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private UserAccountRoleRepository userAccountRoleRepository;

    @Spy
    private EmailValidator emailValidator;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonService personService;

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private EmailConfirmationTokenService emailConfirmationTokenService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private AppUtil appUtil;

    @Mock
    private EmailSender emailSender;

    @Mock
    private LocalDateTime localDateTime;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private AuthTokenFilter authTokenFilter;

    @Mock
    private JwtUserDetails jwtUserDetails;

    @Mock
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    @Mock
    private Clock clock;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void afterEach() {
        reset();
    }

    @Test
    void registerUser_returnBadRequest_WhenUserNameIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username("")
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Username is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenPasswordIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password("")
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Password is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenEmailIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email("")
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Email is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenFirstNameIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName("")
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Firstname is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenLastNameIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName("")
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Lastname is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenCountryIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country("")
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Country is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenIdentityCodeIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode("")
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Identity code is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenPhoneNumberIsEmpty() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber("")
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Phone number is required.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenCalcutagedAgeIsLess18yearsOld() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(LocalDate.now())
            .build();

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: You are less than 18 years old.");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenProvidedUserNameIsTaken() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        given(userAccountRepository.existsByUsername(USERNAME)).willReturn(true);

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Username is already taken!");

        //when
        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenProvidedIdentityCodeIsTaken() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        given(userAccountRepository.existsByIdentityCode(IDENTITY_CODE)).willReturn(true);

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Account with this identity code is already exists.");

        //when
        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenProvidedEmailIsTaken() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        given(userAccountRepository.existsByEmail(EMAIL)).willReturn(true);

        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Email is already in use!");

        //when
        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenEmailIsNotValidViaRegexPattern() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email("emailWithoutAt")
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();


        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Email is not valid.");

        //when
        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenUserNameLengthIsLessThan6Characters() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username("test")
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();


        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Username minimum length is 6.");

        //when
        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnBadRequest_WhenPasswordLengthIsLessThan6Characters() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password("test")
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();


        ResponseEntity<Object> expectResponse = ResponseEntity.badRequest().body("Error: Password minimum length is 6.");

        //when
        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnOkRequest_WhenUserAccountSuccessfullyRegisteredWithExistPersonData() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();

        Person testPerson = Person.builder()
            .id(1L)
            .identityCode(IDENTITY_CODE)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .dateOfBirth(DATE_OF_BIRTH)
            .phoneNumber(PHONE_NUMBER)
            .build();

        UserAccountRole userAccountRole = UserAccountRole.builder().id(1).roleType(UserAccountRoleType.ROLE_DEFAULT_USER).build();

        given(userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_DEFAULT_USER)).willReturn(Optional.ofNullable(userAccountRole));
        given(personRepository.findPersonByIdentityCode(IDENTITY_CODE)).willReturn(Optional.ofNullable(testPerson));
        given(personRepository.existsPersonByIdentityCode(IDENTITY_CODE)).willReturn(true);
        given(appUtil.getUuidString()).willReturn("493410b3-dd0b-4b78-97bf-289f50f6e744");

        //when
        ResponseEntity<Object> expectResponse = ResponseEntity.ok().body("493410b3-dd0b-4b78-97bf-289f50f6e744");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);

    }

    @Test
    void registerUser_returnOkRequest_WhenUserAccountSuccessfullyRegisteredWithNewPersonData() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .build();


        UserAccountRole userAccountRole = UserAccountRole.builder().id(1).roleType(UserAccountRoleType.ROLE_DEFAULT_USER).build();

        given(userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_DEFAULT_USER)).willReturn(Optional.ofNullable(userAccountRole));
        given(personRepository.existsPersonByIdentityCode(IDENTITY_CODE)).willReturn(false);
        given(appUtil.getUuidString()).willReturn("493410b3-dd0b-4b78-97bf-289f50f6e744");

        //when
        ResponseEntity<Object> expectResponse = ResponseEntity.ok().body("493410b3-dd0b-4b78-97bf-289f50f6e744");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void registerUser_returnOkRequest_WhenUserAccountSuccessfullyRegisteredWithNonExistUserRole() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .role(Set.of("testRole"))
            .build();


        UserAccountRole userAccountRole = UserAccountRole.builder().id(1).roleType(UserAccountRoleType.ROLE_DEFAULT_USER).build();

        given(userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_DEFAULT_USER)).willReturn(Optional.ofNullable(userAccountRole));
        given(personRepository.existsPersonByIdentityCode(IDENTITY_CODE)).willReturn(false);
        given(appUtil.getUuidString()).willReturn("493410b3-dd0b-4b78-97bf-289f50f6e744");

        //when
        ResponseEntity<Object> expectResponse = ResponseEntity.ok().body("493410b3-dd0b-4b78-97bf-289f50f6e744");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);

    }

    @Test
    void registerUser_returnOkRequest_WhenUserAccountSuccessfullyRegisteredWithSuperUserRole() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .role(Set.of("superuser"))
            .build();


        UserAccountRole userAccountRole = UserAccountRole.builder().id(1).roleType(UserAccountRoleType.ROLE_SUPERUSER).build();

        given(userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_SUPERUSER)).willReturn(Optional.ofNullable(userAccountRole));
        given(personRepository.existsPersonByIdentityCode(IDENTITY_CODE)).willReturn(false);
        given(appUtil.getUuidString()).willReturn("493410b3-dd0b-4b78-97bf-289f50f6e744");

        //when
        ResponseEntity<Object> expectResponse = ResponseEntity.ok().body("493410b3-dd0b-4b78-97bf-289f50f6e744");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);

    }

    @Test
    void registerUser_returnOkRequest_WhenUserAccountSuccessfullyRegisteredWithAdminRole() {

        // given
        SignupRequestDTO signupRequestDTO = SignupRequestDTO.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .firstName(FIRSTNAME)
            .lastName(LASTNAME)
            .country(COUNTRY)
            .identityCode(IDENTITY_CODE)
            .phoneNumber(PHONE_NUMBER)
            .dateOfBirth(DATE_OF_BIRTH)
            .role(Set.of("admin"))
            .build();


        UserAccountRole userAccountRole = UserAccountRole.builder().id(1).roleType(UserAccountRoleType.ROLE_ADMIN).build();

        given(userAccountRoleRepository.findByRoleType(UserAccountRoleType.ROLE_ADMIN)).willReturn(Optional.ofNullable(userAccountRole));
        given(personRepository.existsPersonByIdentityCode(IDENTITY_CODE)).willReturn(false);
        given(appUtil.getUuidString()).willReturn("493410b3-dd0b-4b78-97bf-289f50f6e744");

        //when
        ResponseEntity<Object> expectResponse = ResponseEntity.ok().body("493410b3-dd0b-4b78-97bf-289f50f6e744");

        var actualResponse = authService.registerUser(signupRequestDTO);

        assertEquals(expectResponse, actualResponse);

    }

    @Test
    void confirmToken_throwsIllegalStateException_WhenProvidedEmailTokenIsNotExist() {
        String tokenUnderTest = "493410b3-dd0b-4b78-97bf-289f50f6e744";

        given(emailConfirmationTokenService.getToken(tokenUnderTest)).willReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> authService.confirmToken(tokenUnderTest));
        assertEquals("Token not found!", exception.getMessage());
    }

    @Test
    void confirmToken_throwsIllegalStateException_WhenEmailAlreadyConfirmed() {
        String tokenUnderTest = "493410b3-dd0b-4b78-97bf-289f50f6e744";

        UserAccount userAccount = UserAccount.builder()
            .personId(1L)
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .identityCode(IDENTITY_CODE)
            .build();

        EmailConfirmationToken emailConfirmationToken = EmailConfirmationToken.builder()
            .confirmToken(tokenUnderTest)
            .createdAt(LocalDateTime.of(2022, 12, 6, 12, 15))
            .expiresAt(LocalDateTime.of(2022, 12, 6, 12, 30))
            .confirmedAt(LocalDateTime.of(2022, 12, 6, 12, 20))
            .userAccount(userAccount)
            .build();

        given(emailConfirmationTokenService.getToken(tokenUnderTest)).willReturn(Optional.ofNullable(emailConfirmationToken));

        Exception exception = assertThrowsExactly(IllegalStateException.class, () -> authService.confirmToken(tokenUnderTest));
        assertEquals("Email is already confirmed!", exception.getMessage());
    }

    @Test
    void confirmToken_throwsIllegalStateException_WhenEmailConfirmationLinkIsExpired() {
        String tokenUnderTest = "493410b3-dd0b-4b78-97bf-289f50f6e744";

        UserAccount userAccount = UserAccount.builder()
            .personId(1L)
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .identityCode(IDENTITY_CODE)
            .build();

        EmailConfirmationToken emailConfirmationToken = EmailConfirmationToken.builder()
            .confirmToken(tokenUnderTest)
            .createdAt(LocalDateTime.of(2022, 12, 6, 12, 15))
            .expiresAt(LocalDateTime.of(2022, 12, 6, 12, 30))
            .userAccount(userAccount)
            .build();


        given(appUtil.getCurrentLocalDateTime()).willReturn(LocalDateTime.of(2022, 12, 6, 12, 45));

        given(emailConfirmationTokenService.getToken(tokenUnderTest)).willReturn(Optional.ofNullable(emailConfirmationToken));

        Exception exception = assertThrowsExactly(IllegalStateException.class, () -> authService.confirmToken(tokenUnderTest));
        assertEquals("Token is expired", exception.getMessage());

    }

    @Test
    void confirmToken_returnOkResponse_WhenEmailIsSuccessfullyConfirmed() {
        String tokenUnderTest = "493410b3-dd0b-4b78-97bf-289f50f6e744";

        UserAccount userAccount = UserAccount.builder()
            .personId(1L)
            .username(USERNAME)
            .password(PASSWORD)
            .email(EMAIL)
            .identityCode(IDENTITY_CODE)
            .build();

        EmailConfirmationToken emailConfirmationToken = EmailConfirmationToken.builder()
            .confirmToken(tokenUnderTest)
            .createdAt(LocalDateTime.of(2022, 12, 6, 12, 15))
            .expiresAt(LocalDateTime.of(2022, 12, 6, 12, 30))
            .userAccount(userAccount)
            .build();

        given(appUtil.getCurrentLocalDateTime()).willReturn(LocalDateTime.of(2022, 12, 6, 12, 20));
        given(emailConfirmationTokenService.getToken(tokenUnderTest)).willReturn(Optional.ofNullable(emailConfirmationToken));

        ResponseEntity<Object> expectResponse = ResponseEntity.ok().body("Account is confirmed.");

        var actualResponse = authService.confirmToken(tokenUnderTest);

        assertEquals(expectResponse, actualResponse);
    }

    @Test
    void authenticateUser_() {
        LoginRequestDTO loginRequest = LoginRequestDTO.builder()
            .username("testUser")
            .password("testPassword")
            .build();

        given(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
            .willReturn(authentication);

        given(jwtTokenUtil.generateJwtToken(authentication)).willReturn("Token");

        given(authentication.getPrincipal()).willReturn(jwtUserDetails);
        given(jwtUserDetails.getUsername()).willReturn("testUser");
        given(jwtUserDetails.getEmail()).willReturn("testUser@gmail.com");

        var actualResponse = authService.authenticateUser(loginRequest);

        JwtResponseDTO expectResponse = new JwtResponseDTO("Token", jwtUserDetails.getUsername(), jwtUserDetails.getEmail(), List.of());

        assertEquals(expectResponse, actualResponse);
    }
}
