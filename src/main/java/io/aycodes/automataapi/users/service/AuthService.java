package io.aycodes.automataapi.users.service;

import io.aycodes.automataapi.common.dtos.auth.AuthResponse;
import io.aycodes.automataapi.common.dtos.user.UserLoginDto;

public interface AuthService {

    AuthResponse authenticateUser(final UserLoginDto userLoginDto);
    boolean isEmailAvailable(final String email);
    boolean isUsernameAvailable(final String username);
}
