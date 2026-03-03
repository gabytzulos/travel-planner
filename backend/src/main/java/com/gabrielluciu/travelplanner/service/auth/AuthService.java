package com.gabrielluciu.travelplanner.service.auth;

import com.gabrielluciu.travelplanner.dto.auth.LoginRequest;
import com.gabrielluciu.travelplanner.dto.auth.AuthResponse;
import com.gabrielluciu.travelplanner.dto.auth.RegisterRequest;
import com.gabrielluciu.travelplanner.entity.auth.User;
import com.gabrielluciu.travelplanner.exception.EmailAlreadyInUseException;
import com.gabrielluciu.travelplanner.repository.UserRepository;
import com.gabrielluciu.travelplanner.security.CustomUserDetails;
import com.gabrielluciu.travelplanner.security.JwtClaims;
import com.gabrielluciu.travelplanner.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest registerRequestDto) {

        if (this.userRepository.existsByEmail(registerRequestDto.email())) {
            throw new EmailAlreadyInUseException(registerRequestDto.email());
        }

        User userEntity = User.builder()
                .email(registerRequestDto.email())
                .passwordHash(this.passwordEncoder.encode(registerRequestDto.password()))
                .firstName(registerRequestDto.firstName())
                .lastName(registerRequestDto.lastName())
                .emailVerified(false)
                .enabled(true) // todo: set to false after adding email validation flow
                .build();

        this.userRepository.save(userEntity);

        // todo: after adding email validation, don't generate token at registration
        Map<String, Object> extraClaims = Map.of(
                "unverified", true,
                JwtClaims.EMAIL, userEntity.getEmail(),
                JwtClaims.FIRST_NAME, userEntity.getFirstName(),
                JwtClaims.LAST_NAME, userEntity.getLastName()
        );

        String token = this.jwtService.generateToken(userEntity.getId(), List.of("USER"), extraClaims); // todo: roles

        return AuthResponse.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .token(token)
                .build();
    }

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> extraClaims = Map.of(
                JwtClaims.EMAIL, userDetails.getEmail(),
                JwtClaims.FIRST_NAME, userDetails.getFirstName(),
                JwtClaims.LAST_NAME, userDetails.getLastName()
        );

        String token = this.jwtService.generateToken(userDetails.getId(), List.of("USER"), extraClaims); // todo: roles

        return AuthResponse.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .token(token)
                .build();
    }

}
