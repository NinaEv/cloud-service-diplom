package com.example.cloudservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.cloudservice.config.JwtTokenUtil;
import com.example.cloudservice.model.JwtRequest;
import com.example.cloudservice.model.JwtResponse;
import com.example.cloudservice.repository.JwtAuthenticationRepository;

@Service
@AllArgsConstructor
public class JwtAuthenticationService {

    private JwtAuthenticationRepository jwtAuthenticationRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;

    public JwtResponse createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {
        final String username = jwtRequest.getLogin();
        final String password = jwtRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        jwtAuthenticationRepository.putTokenAndUsername(token, username);
        return new JwtResponse(token);
    }

    public void logoutAuthenticationTokenAndUsername(String authToken) {
        final String token = authToken.substring(7);
        final String username = jwtAuthenticationRepository.getUsernameByToken(token);
        System.out.println(username + " logout");
        jwtAuthenticationRepository.removeTokenAndUsername(token);
    }
}
