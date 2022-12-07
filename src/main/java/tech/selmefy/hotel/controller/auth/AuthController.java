package tech.selmefy.hotel.controller.auth;


import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.selmefy.hotel.controller.auth.dto.request.LoginRequestDTO;
import tech.selmefy.hotel.controller.auth.dto.request.SignupRequestDTO;
import tech.selmefy.hotel.service.auth.AuthService;

import javax.validation.Valid;

@Api(tags = "Auth")
@RestController
@AllArgsConstructor
@RequestMapping(path = "/auth")
public class AuthController {

    public final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.authenticateUser(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@RequestBody SignupRequestDTO signupRequest) {
        return ResponseEntity.ok(authService.registerUser(signupRequest));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Object> confirm(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }
}
