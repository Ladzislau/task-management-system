package by.vlad.gusakov.taskmanagementsystem.converter.payloadToEntity.user;

import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationRequestToUserEntityConverter implements Converter<UserRegistrationRequest, User> {

    @Override
    public User convert(UserRegistrationRequest source) {
        User user = new User();
        user.setEmail(source.getEmail());
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setPassword(source.getPassword());

        return user;
    }
}
