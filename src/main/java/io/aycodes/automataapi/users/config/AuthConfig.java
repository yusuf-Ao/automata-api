package io.aycodes.automataapi.users.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AuthConfig {

    @Value("${security.jwt.issuer}")
    private  String     issuer;
    @Value("${security.jwt.audience}")
    private  String     audience;
    @Value("${security.jwt.secret-key}")
    private  String     secretKey;
    @Value("${security.jwt.token-validity}")
    private  Long       tokenExpiration;
}
