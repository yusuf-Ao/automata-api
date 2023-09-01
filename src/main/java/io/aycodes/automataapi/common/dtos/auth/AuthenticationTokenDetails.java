package io.aycodes.automataapi.common.dtos.auth;


import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Builder
@Data
public class AuthenticationTokenDetails {

    private final String                    tokenId;
    private final String                    subject;
    private final ZonedDateTime             issuedDate;
    private final ZonedDateTime             expirationDate;
}
