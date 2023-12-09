package by.vlad.gusakov.taskmanagementsystem.controller;

import by.vlad.gusakov.taskmanagementsystem.TestDataFactory;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.AuthenticationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.request.auth.UserRegistrationRequest;
import by.vlad.gusakov.taskmanagementsystem.payload.responce.auth.AuthenticationResponse;
import by.vlad.gusakov.taskmanagementsystem.service.AuthenticationService;
import by.vlad.gusakov.taskmanagementsystem.service.UserRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private final String PERFORM_REGISTRATION_ENDPOINT = "/api/auth/registration";
    private final String PERFORM_LOGIN_ENDPOINT = "/api/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserRegistrationService userRegistrationService;

    @Test
    void performRegistration_validRequest_200JwtTokenAndUserIdReturned() throws Exception {
        UserRegistrationRequest userRegistrationRequest = TestDataFactory.createUserRegistrationRequest();
        AuthenticationResponse authenticationResponse = TestDataFactory.createAuthenticationResponse();

        when(userRegistrationService.registerUser(any())).thenReturn(authenticationResponse);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(POST, PERFORM_REGISTRATION_ENDPOINT, userRegistrationRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken", notNullValue()))
                .andExpect(jsonPath("$.userId", notNullValue()));
    }

    @Test
    void performRegistration_invalidRequest_400ValidationErrorReturned() throws Exception {
        /*
         * email is invalid
         * password is invalid
         */
        UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest(
                "test", "test", "test", "");


        MockHttpServletRequestBuilder mockRequest = prepareRequest(POST, PERFORM_REGISTRATION_ENDPOINT, userRegistrationRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors", is(aMapWithSize(2))));
    }

    @Test
    public void performLogin_validRequest_200JwtTokenAndUserIdReturned() throws Exception {
        AuthenticationRequest authenticationRequest = TestDataFactory.createAuthenticationRequest();
        AuthenticationResponse authenticationResponse = TestDataFactory.createAuthenticationResponse();

        when(authenticationService.loginUser(any())).thenReturn(authenticationResponse);

        MockHttpServletRequestBuilder mockRequest = prepareRequest(PATCH, PERFORM_LOGIN_ENDPOINT, authenticationRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken", notNullValue()))
                .andExpect(jsonPath("$.userId", notNullValue()));
    }

    @Test
    void performLogin_invalidRequest_400ValidationErrorReturned() throws Exception {
        /*
         * email is invalid
         * password is invalid
         */
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                "test", "");

        MockHttpServletRequestBuilder mockRequest = prepareRequest(PATCH, PERFORM_LOGIN_ENDPOINT, authenticationRequest);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", notNullValue()))
                .andExpect(jsonPath("$.status", notNullValue()))
                .andExpect(jsonPath("$.fieldErrors", is(aMapWithSize(2))));
    }

    private MockHttpServletRequestBuilder prepareRequest(HttpMethod httpMethod, String endpoint, Object object) throws Exception {
        String requestBody = objectMapper.writeValueAsString(object);

        if (httpMethod.equals(POST)) {
            return post(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody);
        } else if (httpMethod.equals(PATCH)) {
            return patch(endpoint)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(requestBody);
        } else {
            throw new UnsupportedOperationException("Unsupported HTTP method");
        }
    }
}