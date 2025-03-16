package com.payme.gateway.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@AllArgsConstructor
@Service
public class RedisTokenService {
    private static final String TOKEN_PREFIX = "auth_token:";
    private static final Duration EXPIRATION = Duration.ofHours(2); // token expiry time

    private final StringRedisTemplate redisTemplate;

    // Stores a set of tokens for each associated user.
    public void saveToken(String userId, String token){
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForSet().add(key, token);
        redisTemplate.expire(key, EXPIRATION); //
    }

    public Set<String> fetchToken(String userId){
        String key = TOKEN_PREFIX + userId;
        return redisTemplate.opsForSet().members(key);
    }

    // Check if the token exists within the user's token set.
    private boolean isTokenValid(String userId, String token){
        String key = TOKEN_PREFIX + userId;
        return redisTemplate.opsForSet().isMember(key, token);
    }

    public void deleteToken(String userId, String token){
        String key = TOKEN_PREFIX + userId;
        redisTemplate.opsForSet().remove(userId, token);
    }

    public void deleteAllTokens(String userId){
        String key = TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
    }

}
