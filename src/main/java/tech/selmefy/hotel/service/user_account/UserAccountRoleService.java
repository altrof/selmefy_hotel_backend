package tech.selmefy.hotel.service.user_account;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.selmefy.hotel.repository.user_account.UserAccountRoleRepository;

@Service
@AllArgsConstructor
public class UserAccountRoleService {

    private final UserAccountRoleRepository userAccountRoleRepository;


}
