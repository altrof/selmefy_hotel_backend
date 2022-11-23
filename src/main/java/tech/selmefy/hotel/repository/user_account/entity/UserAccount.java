package tech.selmefy.hotel.repository.user_account.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity( name = "user_account" )
@Table(	name = "user_account",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "account_user_name"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "person_identity_code")
    })
public class UserAccount {

    @Id
    @NonNull
    @Column(name = "person_id")
    private Long personId;

    @NonNull
    @NotBlank
    @Column(name = "account_user_name")
    private String username;

    @NonNull
    @NotBlank
    private String password;

    @NotBlank
    @NonNull
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "person_identity_code")
    @NonNull
    private String identityCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserAccountRole> roles = new HashSet<>();

}
