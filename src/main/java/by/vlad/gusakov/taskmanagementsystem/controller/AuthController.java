package by.vlad.gusakov.taskmanagementsystem.controller;

import by.vlad.gusakov.taskmanagementsystem.exception.AuthenticationException;
import by.vlad.gusakov.taskmanagementsystem.exception.UserNotFoundException;
import by.vlad.gusakov.taskmanagementsystem.exception.EmailNotUniqueException;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.service.AuthenticationService;
import by.vlad.gusakov.taskmanagementsystem.service.UserRegistrationService;
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

    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> performRegistration(
            @RequestBody @Valid UserRegistrationRequest userRegistrationRequest) throws EmailNotUniqueException {

        AuthenticationResponse response = userRegistrationService.registerUser(userRegistrationRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/login")
    public ResponseEntity<AuthenticationResponse> performLogin(
            @RequestBody @Valid AuthenticationRequest authenticationRequest) throws AuthenticationException, UserNotFoundException {

        AuthenticationResponse response = authenticationService.loginUser(authenticationRequest);
        return ResponseEntity.ok(response);
    }
}
