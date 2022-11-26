package com.example.cloudservice.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JwtAuthenticationRepository {

    private final Map<String, String> dataTokensAndUsernames = new ConcurrentHashMap<>();

    public void putTokenAndUsername(String token, String username) {
        dataTokensAndUsernames.put(token, username);
    }

    public void removeTokenAndUsername(String token) {
        dataTokensAndUsernames.remove(token);
    }

    public String getUsernameByToken(String token) {
        return dataTokensAndUsernames.get(token);
    }
}
