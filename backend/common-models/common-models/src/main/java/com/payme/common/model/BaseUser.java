package com.payme.common.model;

import com.payme.common.entity.Role;

import java.util.Set;
import java.util.UUID;
// In an effort to make BaseUser independent of
// UserDetails it should have getUsername/Password()
public interface BaseUser {
    UUID getId();
    String getUsername();
    String getPassword();
    boolean isEnabled();
}
