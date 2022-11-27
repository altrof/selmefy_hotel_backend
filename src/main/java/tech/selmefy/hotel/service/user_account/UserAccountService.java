package tech.selmefy.hotel.service.user_account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;
import tech.selmefy.hotel.repository.user_account.UserAccountRepository;
import tech.selmefy.hotel.security.services.JwtUserDetails;

import java.util.List;

@Service
public class UserAccountService implements UserDetailsService {
    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return JwtUserDetails.build(userAccount);
    }

    public int enableUserAccount(String email) {
        return userAccountRepository.enableUserAccount(email);
    }

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

}
