package tech.selmefy.hotel.controller.auth.dto.response;

import lombok.NonNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class JwtResponseDTO {
    @NonNull
    private String token;
    private String type = "Bearer";

    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private List<String> roles;
}
