package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.repository.UserRepository;
import by.vlad.gusakov.taskmanagementsystem.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> foundedByEmail = userRepository.findByEmail(email);
        if (foundedByEmail.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь с данной электронной почтой не найден!");
        }
        return new CustomUserDetails(foundedByEmail.get());
    }
}
