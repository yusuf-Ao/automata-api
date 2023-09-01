package io.aycodes.automataapi.common.dtos.user;


import io.aycodes.automataapi.common.validations.password.PasswordConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters in length")
    private String          username;

    @NotEmpty
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters in length")
    @PasswordConstraint(message = "Password must contain at least one uppercase, lowercase, symbol and number")
    private String          password;
}
