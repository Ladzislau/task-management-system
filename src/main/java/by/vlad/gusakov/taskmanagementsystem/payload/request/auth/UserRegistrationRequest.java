package by.vlad.gusakov.taskmanagementsystem.payload.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegistrationRequest {

    @Schema(
            description = "Адрес электронной почты",
            example = "user@example.com"
    )
    @NotEmpty(message = "Необходимо указать email")
    @Email(message = "Некорректный email")
    @Size(max = 256, message = "Длина email не может превышать 256 символов")
    private String email;

    @Schema(
            description = "Имя пользователя",
            example = "John"
    )
    @NotEmpty(message = "Необходимо указать имя")
    @Size(max = 24, message = "Длина Имени не может превышать 24 символа")
    private String firstName;

    @Schema(
            description = "Фамилия пользователя",
            example = "Doe"
    )
    @NotEmpty(message = "Необходимо указать фамилию")
    @Size(max = 24, message = "Длина Фамилии не может превышать 24 символа")
    private String lastName;

    @Schema(
            description = "Пароль",
            example = "secretPassword123"
    )
    @Size(min = 8, message = "Минимальная длина пароля 8 символов")
    private String password;

}
