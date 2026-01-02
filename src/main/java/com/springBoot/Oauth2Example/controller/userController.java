package com.springBoot.Oauth2Example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController {

    @GetMapping("/getUser")
    public Map<String, Object> getUser(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            // Return all attributes received from the OAuth provider
            return oAuth2User.getAttributes();
        }
        // If not OAuth2 login, fallback
        return Map.of("username", authentication.getName());
    }
}
