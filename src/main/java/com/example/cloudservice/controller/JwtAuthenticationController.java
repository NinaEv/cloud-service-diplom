package com.example.cloudservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.cloudservice.model.JwtRequest;
import com.example.cloudservice.model.JwtResponse;
import com.example.cloudservice.service.JwtAuthenticationService;

@RestController
@AllArgsConstructor
public class JwtAuthenticationController {

    private JwtAuthenticationService jwtAuthenticationService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JwtResponse login(@RequestBody JwtRequest jwtRequest) {
        return jwtAuthenticationService.createAuthenticationToken(jwtRequest);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String authToken) {
        jwtAuthenticationService.logoutAuthenticationTokenAndUsername(authToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
