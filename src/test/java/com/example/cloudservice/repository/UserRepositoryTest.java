package com.example.cloudservice.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.cloudservice.model.UserDao;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    public static final String USERNAME_1 = "Username1";
    public static final String PASSWORD_1 = "Password1";
    public static final UserDao USER_1 = new UserDao(USERNAME_1, PASSWORD_1, null);

    public static final String USERNAME_2 = "Username2";

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(USER_1);
    }

    @Test
    void findByUsername() {
        assertEquals(USER_1, userRepository.findByUsername(USERNAME_1));
    }

    @Test
    void notFoundByUsername() {
        assertNull(userRepository.findByUsername(USERNAME_2));
    }
}
