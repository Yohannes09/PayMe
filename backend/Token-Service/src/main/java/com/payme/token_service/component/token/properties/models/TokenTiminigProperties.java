package com.payme.token_service.component.token.properties.models;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents timing properties for a token, including validity and issue delay.
 * Used for both access and refresh tokens.
 */
@Getter
@Setter
public class TokenTiminigProperties {
    private int validityMins;
    private int issueAtDelayMins;
}
