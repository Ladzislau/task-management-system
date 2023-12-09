package by.vlad.gusakov.taskmanagementsystem.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class JWTUtilTest {
    private JWTUtil jwtUtil;

    @Value("${jwt_secret}")
    private String secret;

    @BeforeEach
    public void setUp() {
        jwtUtil = new JWTUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", secret);
    }

    @Test
    public void generateToken_shouldGenerateValidToken() {
        String email = "test@example.com";
        String generatedToken = jwtUtil.generateToken(email);

        assertTrue(generatedToken != null && !generatedToken.isEmpty());
    }

    @Test
    public void validateTokenAndRetrieveClaim_shouldReturnCorrectEmail() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email);

        String validatedEmail = jwtUtil.validateTokenAndRetrieveClaim(token);

        assertEquals(email, validatedEmail);
    }

}