package com.articleplatform.article_platform.user.service;


import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(
            String username,
            String email,
            String password,
            String displayName
    ) {

        String normalizedUsername = username.trim().toLowerCase();
        String normalizedEmail = email.trim().toLowerCase();

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new IllegalArgumentException("Username is already taken");
        }

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = new User();

        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setDisplayName(displayName.trim());
        user.setRole("USER");
        user.setEnabled(true);

        return userRepository.save(user);
    }
}

