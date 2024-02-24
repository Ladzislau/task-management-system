package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.EmailNotUniqueException;
import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.repository.UserRepository;
import by.vlad.gusakov.taskmanagementsystem.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ConversionService conversionService;

    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {
        this.userRegistrationService = new UserRegistrationService(userRepository, jwtUtil, passwordEncoder, conversionService);
    }

    @Test
    void registerUser_validRequest_shouldPerformRegistrationAndReturnAuthenticationResponse() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        AuthenticationResponse expected = new AuthenticationResponse("jwt-token", 1L);

        when(conversionService.convert(registrationRequest, User.class)).thenReturn(user);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded");
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("jwt-token");
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(expected, userRegistrationService.registerUser(registrationRequest));
    }

    @Test
    void registerUser_notUniqueEmail_shouldThrowEmailNotUniqueException() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();

        when(conversionService.convert(registrationRequest, User.class)).thenReturn(user);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        assertThrows(EmailNotUniqueException.class, () -> userRegistrationService.registerUser(registrationRequest));
    }
}