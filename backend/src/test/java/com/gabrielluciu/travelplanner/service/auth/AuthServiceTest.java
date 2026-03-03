package com.gabrielluciu.travelplanner.service.auth;

import com.gabrielluciu.travelplanner.dto.auth.AuthResponse;
import com.gabrielluciu.travelplanner.dto.auth.LoginRequest;
import com.gabrielluciu.travelplanner.dto.auth.RegisterRequest;
import com.gabrielluciu.travelplanner.entity.auth.User;
import com.gabrielluciu.travelplanner.exception.EmailAlreadyInUseException;
import com.gabrielluciu.travelplanner.repository.UserRepository;
import com.gabrielluciu.travelplanner.security.CustomUserDetails;
import com.gabrielluciu.travelplanner.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;


    @Test
    void register_success() {
        // Arrange
        RegisterRequest requestDto = new RegisterRequest(
                "test@email.com",
                "FirstName",
                "LastName",
                "password"
        );

        UUID userIdMock = UUID.randomUUID();
        String jwtMock = "jwtMock";
        String encodedPassword = "encodedPassword";
        when(userRepository.existsByEmail(requestDto.email())).thenReturn(false);
        when(passwordEncoder.encode(requestDto.password())).thenReturn(encodedPassword);

        // set the id on the entity
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(userIdMock);
            return u;
        });

        when(jwtService.generateToken(eq(userIdMock), any(), anyMap())).thenReturn(jwtMock);

        // Act
        AuthResponse responseDto = this.authService.register(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals(userIdMock, responseDto.id());
        assertEquals(requestDto.email(), responseDto.email());
        assertEquals(requestDto.firstName(), responseDto.firstName());
        assertEquals(requestDto.lastName(), responseDto.lastName());
        assertEquals(jwtMock, responseDto.token());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(requestDto.password());
    }

    @Test
    void register_emailAlreadyExists_throwsException() {
        // Arrange
        RegisterRequest requestDto = new RegisterRequest(
                "test@email.com",
                "FirstName",
                "LastName",
                "password"
        );

        when(userRepository.existsByEmail(requestDto.email())).thenReturn(true);

        // Act & Assert
        EmailAlreadyInUseException exception = assertThrows(EmailAlreadyInUseException.class, () -> authService.register(requestDto));

        assertTrue(exception.getMessage().contains(requestDto.email()));

        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(any());
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_success() {
        // Arrange
        LoginRequest requestDto = new LoginRequest("test@email.com", "password");
        UUID userIdMock = UUID.randomUUID();
        String jwtMock = "jwtMock";

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userIdMock);
        when(userDetails.getEmail()).thenReturn("test@email.com");
        when(userDetails.getFirstName()).thenReturn("FirstName");
        when(userDetails.getLastName()).thenReturn("LastName");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken(eq(userIdMock), any(), any())).thenReturn(jwtMock);

        // Act
        AuthResponse responseDto = this.authService.login(requestDto);

        // Assert
        assertNotNull(responseDto);
        assertEquals(userIdMock, responseDto.id());
        assertEquals(requestDto.email(), responseDto.email());
        assertEquals("FirstName", responseDto.firstName());
        assertEquals("LastName", responseDto.lastName());
        assertEquals(jwtMock, responseDto.token());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(eq(userIdMock), any(), anyMap());
    }

}
