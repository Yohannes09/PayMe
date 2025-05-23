package com.payme.token.repository;

import com.payme.token.model.KeyRecord;

public interface PublicKeyStore<T extends KeyRecord> {
    T save(T t);
}
