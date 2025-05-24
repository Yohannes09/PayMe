package com.payme.token.persistence;

import com.payme.token.model.PublicKeyRecord;

import java.util.List;

public interface PublicKeyHistory {
    void addKey(PublicKeyRecord publicKeyRecord);

    List<PublicKeyRecord> getKeyHistory();
}
