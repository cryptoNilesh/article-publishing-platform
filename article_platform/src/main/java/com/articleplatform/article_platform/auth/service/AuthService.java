package com.articleplatform.article_platform.auth.service;

import com.articleplatform.article_platform.auth.dto.request.LoginRequest;
import com.articleplatform.article_platform.auth.dto.request.RegisterRequest;
import com.articleplatform.article_platform.auth.dto.response.AuthResponse;
import com.articleplatform.article_platform.exception.DuplicateResourceException;
import com.articleplatform.article_platform.exception.NotFoundException;
import com.articleplatform.article_platform.security.JwtService;
import com.articleplatform.article_platform.user.entity.User;
import com.articleplatform.article_platform.user.repository.UserRepository;
import com.articleplatform.article_platform.user.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class AuthService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserService userService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {

        String username = request.getUsername()
                .trim()
                .toLowerCase(Locale.ROOT);

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException(
                    "Username is already registered"
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Email is already registered"
            );
        }

        User user = userService.registerUser(
                username,
                email,
                request.getPassword(),
                request.getDisplayName()
        );

        String token = jwtService.generateToken(user);

        return buildAuthResponse(user, token);
    }

    public AuthResponse login(LoginRequest request) {

        String usernameOrEmail = request.getUsernameOrEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() ->
                        new BadCredentialsException(
                                "Invalid username/email or password"
                        )
                );

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new BadCredentialsException(
                    "Your account is disabled"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new BadCredentialsException(
                    "Invalid username/email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return buildAuthResponse(user, token);
    }

    private AuthResponse buildAuthResponse(User user, String token) {

        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName()
        );
    }
}


