package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.user;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserRegistrationRequestToUserEntityConverterTest {


    private UserRegistrationRequestToUserEntityConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new UserRegistrationRequestToUserEntityConverter();
    }

    @Test
    public void shouldReturnCommentEntity_whenConvert_giveUserRegistrationRequest() {
        // given
        UserRegistrationRequest userRegistrationRequest = TestDataFactory.createUserRegistrationRequest();
        User expected = TestDataFactory.createNewUser();

        // when
        User result = converter.convert(userRegistrationRequest);

        // then
        assertThat(result).isEqualTo(expected);
    }
}