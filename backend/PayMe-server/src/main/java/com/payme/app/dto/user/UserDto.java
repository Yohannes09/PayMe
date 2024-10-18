package com.payme.app.dto.user;

import com.payme.app.util.TenmoRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private Set<TenmoRoles> role;
    private boolean isActive;
}
