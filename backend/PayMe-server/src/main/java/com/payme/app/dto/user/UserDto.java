package com.payme.app.dto.user;

import com.payme.app.dto.account.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private List<AccountDto> accounts;
    private boolean isActive;
}
