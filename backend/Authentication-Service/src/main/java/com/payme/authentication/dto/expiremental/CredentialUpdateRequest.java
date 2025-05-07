package com.payme.authentication.dto.expiremental;

import java.util.UUID;

public interface CredentialUpdateRequest {
    UUID getId();
    String getNewCredential();
}
