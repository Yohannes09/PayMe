package com.tenmo.dto.user;

import com.tenmo.util.TenmoRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private TenmoRoles role;
    private boolean isActive;
}
