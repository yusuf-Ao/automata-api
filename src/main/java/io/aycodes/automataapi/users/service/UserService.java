package io.aycodes.automataapi.users.service;


import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.user.UserSignupDto;
import io.aycodes.automataapi.users.model.User;

import java.util.Optional;

public interface UserService {

    User createUser(final UserSignupDto userSignupDto) throws CustomException;

    Optional<User> getUserById(final long userId);
}
