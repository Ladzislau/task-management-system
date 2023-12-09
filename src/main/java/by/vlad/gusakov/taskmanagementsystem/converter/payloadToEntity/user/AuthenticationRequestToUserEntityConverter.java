package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.user;

import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationRequestToUserEntityConverter implements Converter<AuthenticationRequest, User> {

    @Override
    public User convert(AuthenticationRequest source) {
        User user = new User();
        user.setEmail(source.getEmail());
        user.setPassword(source.getPassword());

        return user;
    }
}
