package by.vlad.gusakov.taskmanagementsystem.payload.responce.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    @Schema(
            description = "Токен  аутентификации",
            example = "eyJhbGciOiJIUzI1NiIsBUInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIGRldGFweMiLCJlbWFpbCI6ImF1dGhvckBtYWlsLmNvbSIsImlhdCI6MTcwMjEKJBDEoA5OCwiaXNzIjoidGFzay1tYW5hZ2VtZW50LXN5c3RlbS1yZXN0LWF1029dsb6MTcwNDcxNTA5OH0.D9OPjqbBJBcdpN25q0khcPEjHmf8CRPaqDdMFm86LJv28"
    )
    private String jwtToken;

    @Schema(
            description = "ID пользователя, прошедшего регистрацию/авторизацию",
            example = "1"
    )
    private Long userId;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AuthenticationResponse that = (AuthenticationResponse) object;
        return Objects.equals(jwtToken, that.jwtToken) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
