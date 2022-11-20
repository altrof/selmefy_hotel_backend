package tech.selmefy.hotel.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.selmefy.hotel.repository.user_account.entity.UserAccount;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class JwtUserDetails implements UserDetails {
	@Serial
	private static final long serialVersionUID = 1L;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public static JwtUserDetails build(UserAccount userAccount) {
		List<GrantedAuthority> authorities = userAccount.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getRoleType().toString()))
				.collect(Collectors.toList());

		return new JwtUserDetails(
				userAccount.getUsername(),
				userAccount.getEmail(),
				userAccount.getPassword(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
