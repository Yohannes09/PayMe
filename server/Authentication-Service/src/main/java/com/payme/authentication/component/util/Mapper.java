package com.payme.authentication.component.util;

import com.payme.authentication.dto.UserDto;
import com.payme.authentication.entity.Role;
import com.payme.authentication.entity.User;
import com.payme.authentication.entity.model.UserPrincipal;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Mapper {
    private final ModelMapper modelMapper;

    private static Mapper instance;

    @PostConstruct
    public void initialize(){
        instance = new Mapper(modelMapper);
    }

    public static <T> T map(Object sourceType, Class<T> destinationType){
        if(instance == null || instance.modelMapper == null){
            throw new IllegalStateException("Failed to initialize Mapper. ");
        }
        return instance.modelMapper.map(sourceType, destinationType);
    }

    public static UserDto entityToDto(User user){
        Set<String> roles = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(roles)
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .build();
    }

    public static UserPrincipal dtoToPrincipal(UserDto user){
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .build();
    }

    public static UserDto principalToDto(UserPrincipal user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .accountNonExpired(user.isAccountNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .enabled(user.isEnabled())
                .build();
    }

}
/**
 * Mistakes to avoid (from original version):
 *
 * 1. Static Injection Failure:
 *    - Spring can't inject dependencies into static fields (e.g., static ModelMapper).
 *
 * 2. Misuse of @Component:
 *    - Marking the class as @Component does nothing if all methods/fields are static.
 *
 * Proper Fix Summary:
 * - Use constructor injection for ModelMapper.
 * - Capture the injected instance in a static field using @PostConstruct.
 * - Expose static methods that internally use the injected instance.
 */