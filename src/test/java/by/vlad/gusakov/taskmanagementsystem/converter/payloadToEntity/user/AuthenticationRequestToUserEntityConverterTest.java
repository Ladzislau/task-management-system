package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.user;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationRequestToUserEntityConverterTest {

    private AuthenticationRequestToUserEntityConverter converter;

    @BeforeEach
    public void setUp() {
        converter = new AuthenticationRequestToUserEntityConverter();
    }

    @Test
    public void shouldReturnCommentEntity_whenConvert_giveAuthenticationRequest() {
        // given
        AuthenticationRequest authenticationRequest = TestDataFactory.createAuthenticationRequest();
        User expected = TestDataFactory.createAuthenticatedUser();

        // when
        User result = converter.convert(authenticationRequest);

        // then
        assertThat(result).isEqualTo(expected);
    }

}