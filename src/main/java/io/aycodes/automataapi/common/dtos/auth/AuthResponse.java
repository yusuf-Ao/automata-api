package io.aycodes.automataapi.common.dtos.auth;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthResponse {

    private String      accessToken;
}
