package io.aycodes.automataapi.users.controller;


import io.aycodes.automataapi.common.dtos.CustomResponse;
import io.aycodes.automataapi.common.dtos.auth.AuthResponse;
import io.aycodes.automataapi.common.dtos.user.UserLoginDto;
import io.aycodes.automataapi.users.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService           authService;


    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Login user by username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Login successful"),
            @ApiResponse(responseCode = "403", description = "Login failed")
    })
    public ResponseEntity<CustomResponse> userLogin(@Valid @RequestBody final UserLoginDto userLoginDto) {
        try {
            log.info("Attempt user login");
            final String message = "Login successful";
            final AuthResponse auth = authService.authenticateUser(userLoginDto);
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.ACCEPTED.value()).status(HttpStatus.ACCEPTED)
                    .message(message).success(true)
                    .data(auth)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (final Exception e) {
            final String message = "Login Failed";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.FORBIDDEN.value()).status(HttpStatus.FORBIDDEN)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/email-availability")
    @Operation(summary = "Check Email Availability", description = "Check Email Availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email is available"),
            @ApiResponse(responseCode = "417", description = "Email already in use")
    })
    public ResponseEntity<CustomResponse> checkEmailAvailability(@NotNull @RequestParam("email") final String email) {
        try {
            log.info("Attempting to check email availability");
            boolean emailAvailable = authService.isEmailAvailable(email);
            if (!emailAvailable) {
                throw new Exception();
            }
            final String message = "Email is available";
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Email is not available";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }


    @GetMapping("/username-availability")
    @Operation(summary = "Check Username Availability", description = "Check Username Availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username is available"),
            @ApiResponse(responseCode = "417", description = "Username already in use")
    })
    public ResponseEntity<CustomResponse> checkUsernameAvailability(@NotNull @RequestParam("username") final String username) {
        try {
            log.info("Attempting to check username availability");
            boolean usernameAvailable = authService.isUsernameAvailable(username);
            if (!usernameAvailable) {
                throw new Exception();
            }
            final String message = "Username is available";
            final CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value()).status(HttpStatus.OK)
                    .message(message).success(true)
                    .build();
            log.info(message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (final Exception e) {
            final String message = "Username is not available";
            log.error(message, e);
            CustomResponse response = CustomResponse.builder().timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.EXPECTATION_FAILED.value()).status(HttpStatus.EXPECTATION_FAILED)
                    .message(message).success(false)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }
}
