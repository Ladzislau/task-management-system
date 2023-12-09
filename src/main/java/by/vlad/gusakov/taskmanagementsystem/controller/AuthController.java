package by.vlad.gusakov.taskmanagementsystem.controller;

import by.vlad.gusakov.taskmanagementsystem.exception.AuthenticationException;
import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.exception.EmailNotUniqueException;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ValidationErrorResponse;
import by.vlad.gusakov.taskmanagementsystem.service.AuthenticationService;
import by.vlad.gusakov.taskmanagementsystem.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, UserRegistrationService userRegistrationService) {
        this.authenticationService = authenticationService;
        this.userRegistrationService = userRegistrationService;
    }

    @Operation(
            summary = "Регистрация пользователя",
            description = "Для регистрации пользователь должен указать уникальный email, свое имя, фамилию и пароль ",
            responses = {
                    @ApiResponse(
                            description = "Регистрация прошла успешно",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Регистрация не удалась, поле(-я) запроса содержит(-ат) ошибки",
                            responseCode = "400",
                            content =  @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> performRegistration(
            @RequestBody @Valid UserRegistrationRequest userRegistrationRequest) throws EmailNotUniqueException {

        AuthenticationResponse response = userRegistrationService.registerUser(userRegistrationRequest);
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Вход в систему",
            description = "Для входа в систему пользователь должен указать email и пароль от его учетной записи",
            responses = {
                    @ApiResponse(
                            description = "Вход в систему прошел успешно",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Не удалось войти в систему, поле(-я) запроса содержит(-ат) ошибки",
                            responseCode = "400",
                            content =  @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ValidationErrorResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/login")
    public ResponseEntity<AuthenticationResponse> performLogin(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) throws AuthenticationException, UserNotFoundException {

        AuthenticationResponse response = authenticationService.loginUser(authenticationRequest);
        return ResponseEntity.ok(response);
    }
}
