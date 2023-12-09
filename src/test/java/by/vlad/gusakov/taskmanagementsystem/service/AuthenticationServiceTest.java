package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.AuthenticationException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.util.JWTUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @MockBean
    private AuthenticationProvider authProvider;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private ConversionService conversionService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp(){
        this.authenticationService = new AuthenticationService(authProvider, userService, jwtUtil, conversionService);
    }

    @Test
    void loginUser_goodCredentials_shouldPerformLoginAndReturnAuthenticationResponse() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        AuthenticationResponse expected = new AuthenticationResponse("jwt-token", 1L);

        when(conversionService.convert(authenticationRequest, User.class)).thenReturn(user);
        when(userService.findByEmail(user.getEmail())).thenReturn(user);
        when(jwtUtil.generateToken(user.getEmail())).thenReturn("jwt-token");

        assertEquals(expected, authenticationService.loginUser(authenticationRequest));
    }

    @Test
    void loginUser_badCredentials_shouldThrowBadCredentialsException() throws Exception {
        User user = new User();

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        when(conversionService.convert(authenticationRequest, User.class)).thenReturn(user);
        when(authProvider.authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(AuthenticationException.class, () -> authenticationService.loginUser(authenticationRequest));
    }

}