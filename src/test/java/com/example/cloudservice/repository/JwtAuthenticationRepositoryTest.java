package com.example.cloudservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationRepositoryTest {

    public static final String TOKEN_1 = "Token1";
    public static final String TOKEN_2 = "Token2";
    public static final String USERNAME_1 = "Username1";
    public static final String USERNAME_2 = "Username2";

    private JwtAuthenticationRepository jwtAuthenticationRepository;

    private final Map<String, String> testDataTokensAndUsernames = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        jwtAuthenticationRepository = new JwtAuthenticationRepository();
        jwtAuthenticationRepository.putTokenAndUsername(TOKEN_1, USERNAME_1);
        testDataTokensAndUsernames.clear();
        testDataTokensAndUsernames.put(TOKEN_1, USERNAME_1);
    }

    @Test
    void putTokenAndUsername() {
        String beforePut = jwtAuthenticationRepository.getUsernameByToken(TOKEN_2);
        assertNull(beforePut);
        jwtAuthenticationRepository.putTokenAndUsername(TOKEN_2, USERNAME_2);
        String afterPut = jwtAuthenticationRepository.getUsernameByToken(TOKEN_2);
        assertEquals(USERNAME_2, afterPut);
    }

    @Test
    void removeTokenAndUsername() {
        String beforeRemove = jwtAuthenticationRepository.getUsernameByToken(TOKEN_1);
        assertNotNull(beforeRemove);
        jwtAuthenticationRepository.removeTokenAndUsername(TOKEN_1);
        String afterRemove = jwtAuthenticationRepository.getUsernameByToken(TOKEN_1);
        assertNull(afterRemove);
    }

    @Test
    void getUsernameByToken() {
        assertEquals(testDataTokensAndUsernames.get(TOKEN_1), jwtAuthenticationRepository.getUsernameByToken(TOKEN_1));
    }
}

