package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.AuthenticationException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationProvider authProvider;

    private final UserService userService;

    private final JWTUtil jwtUtil;

    private final ConversionService conversionService;

    public AuthenticationResponse loginUser(AuthenticationRequest authenticationRequest) {
        User user = conversionService.convert(authenticationRequest, User.class);

        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());

        try {
            authProvider.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Ошибка авторизации", "Неправильный email или пароль");
        }
        User userToLogin = userService.findByEmail(user.getEmail());
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthenticationResponse(token, userToLogin.getId());
    }
}
