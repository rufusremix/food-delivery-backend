package com.rufus.store.auth;

import com.rufus.store.users.Role;
import com.rufus.store.users.User;
import com.rufus.store.users.UserMapper;
import com.rufus.store.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return generateLoginResponse(user);
    }

    /**
     * Smart auth: login if user exists, register if not.
     * Wrong password → throws BadCredentialsException (handled by GlobalExceptionHandler).
     */
    public LoginResponse signIn(LoginRequest request) {
        var existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            // User exists → verify password and login
            if (!passwordEncoder.matches(request.getPassword(), existingUser.get().getPassword())) 
                throw new BadCredentialsException("Invalid credentials");
                
            return generateLoginResponse(existingUser.get());
        } else {
            // User doesn't exist → auto-register then login
            var user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getEmail().split("@")[0]);
            user.setRole(Role.USER);
            userRepository.save(user);
            return generateLoginResponse(user);
        }
    }

    private LoginResponse generateLoginResponse(User user) {
        return new LoginResponse(
                jwtService.generateAccessToken(user),
                jwtService.generateRefreshToken(user)
        );
    }

    public Jwt refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidRefreshTokenException();
        }

        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new InvalidRefreshTokenException();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return jwtService.generateAccessToken(user);
    }
}