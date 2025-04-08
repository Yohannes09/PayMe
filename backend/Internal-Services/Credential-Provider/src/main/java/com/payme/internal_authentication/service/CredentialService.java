package com.payme.internal_authentication.service;

import com.payme.internal_authentication.entity.Client;
import com.payme.internal_authentication.entity.Credential;

public interface CredentialService {
    Credential resolveCredential(Client client);
}
