package com.gabrielluciu.travelplanner.controller.auth;

import com.gabrielluciu.travelplanner.dto.auth.LoginRequestDto;
import com.gabrielluciu.travelplanner.dto.auth.LoginResponseDto;
import com.gabrielluciu.travelplanner.dto.auth.RegisterRequestDto;
import com.gabrielluciu.travelplanner.dto.auth.AuthResponseDto;
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
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        AuthResponseDto authResponse = this.authService.login(loginRequestDto);

        ResponseCookie jwtCookie = generateJwtCookie(authResponse.token());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new LoginResponseDto(authResponse);
    }

    @PostMapping("/register")
    public LoginResponseDto register(@Valid @RequestBody RegisterRequestDto registerRequestDto, HttpServletResponse response) {
        AuthResponseDto authResponse = this.authService.register(registerRequestDto);

        // todo: remove after adding email validation
        ResponseCookie jwtCookie = generateJwtCookie(authResponse.token());
        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return new LoginResponseDto(authResponse);
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
