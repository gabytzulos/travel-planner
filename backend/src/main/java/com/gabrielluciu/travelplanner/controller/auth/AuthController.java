package com.gabrielluciu.travelplanner.controller.auth;

import com.gabrielluciu.travelplanner.dto.auth.LoginRequest;
import com.gabrielluciu.travelplanner.dto.auth.LoginResponse;
import com.gabrielluciu.travelplanner.dto.auth.RegisterRequest;
import com.gabrielluciu.travelplanner.dto.auth.AuthResponse;
import com.gabrielluciu.travelplanner.security.SecurityConstants;
import com.gabrielluciu.travelplanner.service.auth.AuthService;
import com.gabrielluciu.travelplanner.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = this.authService.login(request);

        ResponseCookie jwtCookie = generateJwtCookie(authResponse.token());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new LoginResponse(authResponse);
    }

    @PostMapping("/register")
    public LoginResponse register(@Valid @RequestBody RegisterRequest registerRequestDto, HttpServletResponse response) {
        AuthResponse authResponse = this.authService.register(registerRequestDto);

        // todo: remove after adding email validation
        ResponseCookie jwtCookie = generateJwtCookie(authResponse.token());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new LoginResponse(authResponse);
    }

    private ResponseCookie generateJwtCookie(String token) {
        return ResponseCookie.from(SecurityConstants.JWT_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(this.jwtService.getExpirationMs() / 1000)
                .sameSite("Lax")
                .build();
    }

}
