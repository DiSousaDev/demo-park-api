package br.dev.diego.demoparkapi.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final Logger log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("Authentication required. Sending 401 Unauthorized response. Error: {}", authException.getMessage());
        response.setHeader("www-authenticate", "Bearer realm=\"/auth\", error=\"invalid_token\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
