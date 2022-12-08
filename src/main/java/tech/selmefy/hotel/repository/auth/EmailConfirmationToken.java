package tech.selmefy.hotel.repository.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String confirmToken;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @NonNull
    @ManyToOne
    @JoinColumn(
        nullable = false,
        name = "user_account_id"
    )
    private UserAccount userAccount;
}
