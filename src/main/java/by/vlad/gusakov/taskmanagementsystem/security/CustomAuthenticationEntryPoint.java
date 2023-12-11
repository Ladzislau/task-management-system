package by.vlad.gusakov.taskmanagementsystem.security;

import by.vlad.gusakov.taskmanagementsystem.payload.responce.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private final LocaleResolver localeResolver;

    @Autowired
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper, LocaleResolver localeResolver) {
        this.objectMapper = objectMapper;
        this.localeResolver = localeResolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        int errorStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ErrorResponse errorResponse = new ErrorResponse("Ошибка сервера", errorStatus, "Сервер непредвиденно вернул ошибку! Проверьте правильность отправляемых данных, проверьте авторизированы ли вы");

        response.setStatus(errorStatus);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Locale currentLocale = localeResolver.resolveLocale(request);

        objectMapper.setLocale(currentLocale);

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
