package tech.selmefy.hotel.repository.user_account.entity;

import lombok.Getter;
import lombok.Setter;
import tech.selmefy.hotel.repository.user_account.type.UserAccountRoleType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "user_roles_type")
public class UserAccountRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private UserAccountRoleType roleType;
}
