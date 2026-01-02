package com.springBoot.Oauth2Example.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBoot.Oauth2Example.dto.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType("application/json");
        MessageResponse response = new MessageResponse( false,HttpStatus.UNAUTHORIZED, "Unauthorized: " + ex.getMessage());
        res.getWriter().write(objectMapper.writeValueAsString(response));
    }
}