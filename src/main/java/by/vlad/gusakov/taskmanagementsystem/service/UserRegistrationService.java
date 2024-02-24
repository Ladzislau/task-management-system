package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.EmailNotUniqueException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.repository.UserRepository;
import by.vlad.gusakov.taskmanagementsystem.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ConversionService conversionService;

    public AuthenticationResponse registerUser(UserRegistrationRequest registrationRequest) throws EmailNotUniqueException {
        User user = conversionService.convert(registrationRequest, User.class);

        if (isEmailAlreadyUsed(user.getEmail())) {
            throw new EmailNotUniqueException("Ошибка регистрации", "Данный email уже занят!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String token = jwtUtil.generateToken(user.getEmail());
        User savedUser = userRepository.save(user);
        return new AuthenticationResponse(token, savedUser.getId());
    }

    private boolean isEmailAlreadyUsed(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }
}
