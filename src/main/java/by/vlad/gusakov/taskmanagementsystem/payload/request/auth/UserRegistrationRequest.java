package by.vlad.gusakov.taskmanagementsystem.payload.request.auth;

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

    @NotEmpty(message = "Необходимо указать email")
    @Email(message = "Некорректный email")
    @Size(max = 256, message = "Длина email не может превышать 256 символов")
    private String email;

    @NotEmpty(message = "Необходимо указать Имя")
    @Size(max = 24, message = "Длина Имени не может превышать 24 символа")
    private String firstName;

    @NotEmpty(message = "Необходимо указать Фамилию")
    @Size(max = 24, message = "Длина Фамилии не может превышать 24 символа")
    private String lastName;

    @Size(min = 8, message = "Минимальная длина пароля 8 символов")
    private String password;

}
