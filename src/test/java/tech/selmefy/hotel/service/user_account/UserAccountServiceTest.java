package tech.selmefy.hotel.service.user_account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech.selmefy.hotel.repository.user_account.UserAccountRepository;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;
import tech.selmefy.hotel.repository.user_account.entity.UserAccountRole;
import tech.selmefy.hotel.security.services.JwtUserDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType.ROLE_ADMIN;
import static tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType.ROLE_DEFAULT_USER;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    @Test
    void loadUserByUsername_throwsUsernameNotFoundException_WhenProvidedUserNameIsNotExist() {
        Exception exception = assertThrows(
            UsernameNotFoundException.class,
            () -> userAccountService.loadUserByUsername("nonExistUser")
        );

        assertEquals("User Not Found with username: nonExistUser", exception.getMessage());
    }

    @Test
    void loadUserByUsername_returnJwtUserDetails_WhenProvidedUserNameIsExist() {

        UserAccountRole userAccountRole = UserAccountRole.builder().id(1).roleType(ROLE_DEFAULT_USER).build();
        UserAccount userAccount = UserAccount.builder()
            .personId(1L)
            .username("testUser")
            .password("testPassword")
            .email("test@email.com")
            .identityCode("AA1234567")
            .roles(Set.of(userAccountRole))
            .build();

        JwtUserDetails jwtUserDetails = JwtUserDetails.build(userAccount);
        mock(JwtUserDetails.class);

        given(userAccountRepository.findByUsername("testUser")).willReturn(Optional.of(userAccount));

        var actualResponse = userAccountService.loadUserByUsername("testUser");

        assertEquals(jwtUserDetails.getClass(), actualResponse.getClass());
    }

    @Test
    void getAllUsers_returnListOfUserAccounts() {
        UserAccountRole userAccountRole1 = UserAccountRole.builder().id(1).roleType(ROLE_DEFAULT_USER).build();
        UserAccount userAccount1 = UserAccount.builder()
            .personId(1L)
            .username("testUser1")
            .password("testPassword1")
            .email("test1@email.com")
            .identityCode("AA1234567")
            .roles(Set.of(userAccountRole1))
            .build();

        UserAccountRole userAccountRole2 = UserAccountRole.builder().id(2).roleType(ROLE_ADMIN).build();
        UserAccount userAccount2 = UserAccount.builder()
            .personId(2L)
            .username("testUser2")
            .password("testPassword2")
            .email("test2@email.com")
            .identityCode("BB1234567")
            .roles(Set.of(userAccountRole2))
            .build();

        List<UserAccount> expectedResult = List.of(userAccount1, userAccount2);

        given(userAccountRepository.findAll()).willReturn(expectedResult);

        var actualResult = userAccountService.getAllUsers();

        assertEquals(expectedResult, actualResult);
        assertEquals(2, actualResult.size());
    }
}