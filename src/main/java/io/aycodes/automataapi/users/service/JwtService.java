package io.aycodes.automataapi.users.service;

import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.auth.AuthResponse;
import io.aycodes.automataapi.common.dtos.auth.AuthenticationTokenDetails;
import io.aycodes.automataapi.users.model.User;

public interface JwtService {

    AuthResponse issueToken(final User user);
    AuthenticationTokenDetails parseToken(final String token) throws CustomException;
}
