package io.aycodes.automataapi.users.service.impl;

import io.aycodes.automataapi.common.dtos.auth.AuthResponse;
import io.aycodes.automataapi.common.dtos.user.UserLoginDto;
import io.aycodes.automataapi.users.model.User;
import io.aycodes.automataapi.users.repository.UserRepo;
import io.aycodes.automataapi.users.service.AuthService;
import io.aycodes.automataapi.users.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo                          userRepo;
    private final JwtService                        jwtService;
    private final AuthenticationManager             authenticationManager;

    @Override
    public AuthResponse authenticateUser(final UserLoginDto userLoginDto) {
        log.info("Attempting to authenticate user");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(),
                        userLoginDto.getPassword()
                )
        );
        User user = userRepo.findByUsername(userLoginDto.getUsername())
                .orElseThrow();
        log.info("Attempting to issue JWT access token to user");
        return jwtService.issueToken(user);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepo.findByUsername(username).isPresent();
    }
}
