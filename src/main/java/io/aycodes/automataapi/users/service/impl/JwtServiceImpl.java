package io.aycodes.automataapi.users.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.auth.AuthResponse;
import io.aycodes.automataapi.common.dtos.auth.AuthenticationTokenDetails;
import io.aycodes.automataapi.common.utility.TimeUtil;
import io.aycodes.automataapi.users.config.AuthConfig;
import io.aycodes.automataapi.users.model.User;
import io.aycodes.automataapi.users.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JwtServiceImpl implements JwtService {

    private final AuthConfig                authConfig;


    @Override
    public AuthResponse issueToken(final User user) {
        final ZonedDateTime issuedDate = TimeUtil.getZonedDateTimeOfInstant();
        final ZonedDateTime expiryDate = calculateTokenExpirationDate(issuedDate);
        log.info("Generating jwt token");
        String token = JWT.create()
                .withJWTId(generateTokenIdentifier())
                .withIssuer(authConfig.getIssuer())
                .withAudience(authConfig.getAudience())
                .withSubject(String.valueOf(user.getUsername()))
                .withIssuedAt(Date.from(issuedDate.toInstant()))
                .withExpiresAt(Date.from(expiryDate.toInstant()))
                .sign(getAlgorithmForToken());
        return AuthResponse.builder()
                .accessToken(token).build();
    }

    @Override
    public AuthenticationTokenDetails parseToken(String token) throws CustomException {
        try {
            Algorithm algorithm       = getAlgorithmForToken();
            JWTVerifier verifier      = JWT.require(algorithm)
                    .withAudience(authConfig.getAudience())
                    .withIssuer(authConfig.getIssuer()).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            return AuthenticationTokenDetails.builder()
                    .tokenId(decodedJWT.getId())
                    .subject(decodedJWT.getSubject())
                    .issuedDate(ZonedDateTime.ofInstant(decodedJWT.getIssuedAtAsInstant(), TimeUtil.getZONE_ID()))
                    .expirationDate(ZonedDateTime.ofInstant(decodedJWT.getExpiresAtAsInstant(), TimeUtil.getZONE_ID()))
                    .build();
        } catch (final Exception e) {
            final String message = "Invalid token";
            log.error(message,e);
            throw new CustomException(HttpStatus.UNAUTHORIZED, message, e);
        }
    }

    private Algorithm getAlgorithmForToken() {
        return Algorithm.HMAC512(authConfig.getSecretKey());
    }

    private String generateTokenIdentifier() {
        return UUID.randomUUID().toString();
    }

    private ZonedDateTime calculateTokenExpirationDate(final ZonedDateTime issuedDate) {
        return issuedDate.plusSeconds(authConfig.getTokenExpiration());
    }
}
