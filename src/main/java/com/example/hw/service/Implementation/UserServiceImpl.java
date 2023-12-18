package com.example.hw.service.Implementation;

import com.example.hw.entities.User;
import com.example.hw.repository.UserRepository;
import com.example.hw.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
