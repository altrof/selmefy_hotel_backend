package tech.selmefy.hotel.controller.user_account;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;
import tech.selmefy.hotel.service.user_account.UserAccountService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping
    public List<UserAccount> getAllUsers() {
         return userAccountService.getAllUsers();
    }
}
