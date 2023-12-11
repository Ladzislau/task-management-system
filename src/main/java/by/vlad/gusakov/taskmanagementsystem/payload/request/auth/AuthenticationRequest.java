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

public class AuthenticationRequest {


    @Schema(
            description = "Адрес электронной почты",
            example = "user@example.com"
    )
    @NotEmpty(message = "Необходимо указать адрес электронной почты")
    @Email(message = "Некорректный адрес электронной почты")
    @Size(max = 256, message = "Длина адреса электронной почты не может превышать 256 символов")
    private String email;

    @Schema(
            description = "Пароль",
            example = "secretPassword123"
    )
    @NotEmpty(message = "Необходимо указать пароль!")
    private String password;
}
