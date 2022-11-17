package tech.selmefy.hotel.controller.auth.dto.response;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "";

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}
