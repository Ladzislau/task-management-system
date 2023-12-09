package by.vlad.gusakov.taskmanagementsystem.service;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;


    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository);
    }

    @Test
    void findByEmail_existingUser_shouldReturnUser() throws Exception {
        User user = TestDataFactory.createNewUser();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertEquals(user, userService.findByEmail(user.getEmail()));
    }

    @Test
    void findByEmail_userNotFound_shouldThrowUserNotFoundException() throws Exception {
        String email = "test@mail.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));
    }

    @Test
    void findById_existingUser_shouldReturnUser() throws Exception {
        Long id = 1L;
        User user = TestDataFactory.createNewUser();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertEquals(user, userService.findById(id));
    }

    @Test
    void findById_userNotFound_shouldThrowUserNotFoundException() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(id));
    }

}