package io.aycodes.automataapi.users.controller;


import io.aycodes.automataapi.common.config.SecurityConfig;
import io.aycodes.automataapi.common.dtos.CustomResponse;
import io.aycodes.automataapi.common.dtos.user.UserSignupDto;
import io.aycodes.automataapi.users.model.User;
import io.aycodes.automataapi.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService           userService;


    @PostMapping("/new-user")
    @Operation(summary = "Create User", description = "Create new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to create user")
    })
    public ResponseEntity<CustomResponse> createUser(@Valid @RequestBody final UserSignupDto userSignupDto) {
        try {
            log.info("Attempt user creation");
            final String message = "successful";
            final User user = userService.createUser(userSignupDto);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.CREATED.value()).status(HttpStatus.CREATED)
                    .message(message).success(true)
                    .data(user)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (final Exception e) {
            final String message = "Unable to create user";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @GetMapping("/user")
    @Operation(summary = "Get User", description = "Retrieve user details",
            security = { @SecurityRequirement(name = "Bearer Token") })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fetched successfully"),
            @ApiResponse(responseCode = "417", description = "Unable to get user details")
    })
    public ResponseEntity<CustomResponse> getUser(SecurityContextHolder securityContextHolder) {
        try {
            log.info("Attempt to fetch user details");
            final String message = "User fetched successfully";
            final Long userId = SecurityConfig.extractUserDetailsFromSecurityContext(securityContextHolder).getId();
            final Optional<User> user = userService.getUserById(userId);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .data(user)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Unable to get user details";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
