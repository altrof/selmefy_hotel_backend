package tech.selmefy.hotel.repository.user_account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByUsername(@Param("username") String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByIdentityCode(String identityCode);

    @Transactional
    @Modifying
    @Query("UPDATE user_account acc SET acc.enabled = TRUE WHERE acc.email = :email")
    int enableUserAccount(@Param("email") String email);
}

