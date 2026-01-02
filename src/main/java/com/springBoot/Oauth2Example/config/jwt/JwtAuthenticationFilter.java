package com.springBoot.Oauth2Example.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBoot.Oauth2Example.dto.MessageResponse;
import com.springBoot.Oauth2Example.entity.User;
import com.springBoot.Oauth2Example.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter implements UserDetailsService {

    private final JwtService jwtHelper;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService helper, UserRepository userRepository) {
        this.jwtHelper = helper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return user;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException{

        ObjectMapper objectMapper = new ObjectMapper();
        res.setContentType("application/json");

        try {
            String header = req.getHeader("Authorization");

            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);

                String username = jwtHelper.getUsername(token);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = (User) loadUserByUsername(username);
                    if (jwtHelper.validateToken(token,user.getUsername())) {

                        if (!token.equals(user.getAuthToken())) {
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            MessageResponse response = new MessageResponse(false, HttpStatus.UNAUTHORIZED, "Session expired: Logged in from another device");
                            String jsonResponse = objectMapper.writeValueAsString(response);
                            res.getWriter().write(jsonResponse);
                            return;
                        }

//                        if (token.equals(user.getAuthToken()) && !user.getAuthToken().isEmpty()) {
//                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
//                            MessageResponse response = new MessageResponse(false, HttpStatus.UNAUTHORIZED, "Logout from other device!");
//                            String jsonResponse = objectMapper.writeValueAsString(response);
//                            res.getWriter().write(jsonResponse);
//                            return;
//                        }

                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }

            chain.doFilter(req, res);

        }  catch (Exception ex) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            MessageResponse response = new MessageResponse(false, HttpStatus.UNAUTHORIZED, "Unauthorized access attempt: " + ex.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(response);
            res.getWriter().write(jsonResponse);
        }
    }
}
