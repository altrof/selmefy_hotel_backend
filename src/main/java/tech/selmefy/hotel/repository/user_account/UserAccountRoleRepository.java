package tech.selmefy.hotel.repository.user_account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.selmefy.hotel.repository.user_account.entity.UserAccountRole;
import tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType;

import java.util.Optional;

@Repository
public interface UserAccountRoleRepository extends JpaRepository<UserAccountRole, Integer> {
    Optional<UserAccountRole> findByRoleType(UserAccountRoleType roleType);
}
