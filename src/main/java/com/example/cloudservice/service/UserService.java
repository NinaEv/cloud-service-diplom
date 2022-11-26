package com.example.cloudservice.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.cloudservice.exception.UnauthorizedException;
import com.example.cloudservice.model.UserDao;
import com.example.cloudservice.repository.UserRepository;


@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao user = userDao.findByUsername(username);
        if (user == null) {
            System.out.println("User Unauthorized");
            throw new UnauthorizedException("User Unauthorized");
        }
        return user;
    }
}
