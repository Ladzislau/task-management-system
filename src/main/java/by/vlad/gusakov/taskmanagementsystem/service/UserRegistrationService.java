package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.exception.EmailNotUniqueException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;

    @Autowired
    public UserRegistrationService(UserService userService, JWTUtil jwtUtil, PasswordEncoder passwordEncoder, ConversionService conversionService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.conversionService = conversionService;
    }

    public AuthenticationResponse registerUser(UserRegistrationRequest registrationRequest) throws EmailNotUniqueException {
        User user = conversionService.convert(registrationRequest, User.class);

        if (emailAlreadyExists(user.getEmail())) {
            throw new EmailNotUniqueException("Ошибка регистрации", "Данный email уже занят!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String token = jwtUtil.generateToken(user.getEmail());
        User savedUser = userService.save(user);
        return new AuthenticationResponse(token, savedUser.getId());
    }

    private boolean emailAlreadyExists(String email) {
        try {
            userService.findByEmail(email);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }
}
