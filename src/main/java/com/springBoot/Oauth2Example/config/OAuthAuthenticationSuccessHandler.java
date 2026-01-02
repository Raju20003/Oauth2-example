package com.springBoot.Oauth2Example.config;

import com.springBoot.Oauth2Example.config.jwt.JwtService;
import com.springBoot.Oauth2Example.controller.userController;
import com.springBoot.Oauth2Example.dto.MessageResponse;
import com.springBoot.Oauth2Example.entity.User;
import com.springBoot.Oauth2Example.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public OAuthAuthenticationSuccessHandler(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        User user = new User();
        user.setIs_deleted(false);
        user.setMobileNo(null);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(null);
        user.setPassword(null);
        user.setPwd(null);

        if(oauthToken.getAuthorizedClientRegistrationId().equals("google")){
            user.setFullName(defaultOAuth2User.getAttribute("name"));
            user.setEmail(defaultOAuth2User.getAttribute("email"));
            user.setAuthToken(jwtService.generateToken(user.getEmail()));
            user.setProvidedId(oauthToken.getAuthorizedClientRegistrationId());
            user.setProviderUserId(defaultOAuth2User.getName());
            user.setPicture(defaultOAuth2User.getAttribute("picture"));
        }

        else if(oauthToken.getAuthorizedClientRegistrationId().equals("github")){
            user.setFullName(defaultOAuth2User.getAttribute("login"));
            user.setEmail(defaultOAuth2User.getAttribute("login"));
            user.setAuthToken(jwtService.generateToken(user.getEmail()));
            user.setProvidedId(oauthToken.getAuthorizedClientRegistrationId());
            user.setProviderUserId(defaultOAuth2User.getName());
            user.setPicture(defaultOAuth2User.getAttribute("avatar_url"));
        }

        if(!userRepository.existsByEmail(user.getEmail())){
            userRepository.save(user);
            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/getUser");
        }
    }
}
