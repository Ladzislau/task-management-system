package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.exception.AuthenticationException;
import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.repository.UserRepository;
import by.vlad.gusakov.taskmanagementsystem.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user){

        return userRepository.save(user);
    }


    public User findByEmail(String email) throws UserNotFoundException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден", "Пользователя с данным email не существует!"));
    }

    public User findById(Long id) throws UserNotFoundException {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден", "Пользователя с данным id не существует!"));
    }

    public User getCurrentUser() throws UserNotFoundException, AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated())
            throw new AuthenticationException("Ошибка выполнения операции", "Для выполнения данной операции необходимо зарегистрироваться или войти в систему");
        CustomUserDetails personDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(personDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден", "Пользователя с данным email не существует!"));
    }
}
