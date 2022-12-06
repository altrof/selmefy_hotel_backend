package tech.selmefy.hotel.controller.auth.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class LoginRequestDTO {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
