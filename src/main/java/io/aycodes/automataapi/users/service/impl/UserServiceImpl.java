package io.aycodes.automataapi.users.service.impl;


import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.user.UserSignupDto;
import io.aycodes.automataapi.users.model.User;
import io.aycodes.automataapi.users.repository.UserRepo;
import io.aycodes.automataapi.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo                  userRepo;
    private final PasswordEncoder           passwordEncoder;

    @Override
    public User createUser(final UserSignupDto userSignupDto) throws CustomException {
        final String email = userSignupDto.getEmail();
        final String username = userSignupDto.getUsername();
        log.info("checking if email exists");
        if (userRepo.findByEmail(email).isPresent()) {
            throw new CustomException("Email is already in use");
        }

        if (userRepo.findByUsername(username).isPresent()) {
            throw new CustomException("Username is already in use");
        }

        try {
            log.info("Now creating user");
            final User user = User.builder()
                    .email(email)
                    .username(username)
                    .password(passwordEncoder.encode(userSignupDto.getPassword()))
                    .build();
            return userRepo.save(user);
        } catch (final Exception e) {
            final String message = "Unable to create user";
            log.error(message, e);
            throw new CustomException(message, e);
        }
    }

    @Override
    public Optional<User> getUserById(final long userId) {
        return userRepo.findById(userId);
    }
}
