package com.payme.authentication.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@Configuration
@EnableCaching
public class CachingConfig {
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisCacheWriter redisCacheWriter = RedisCacheWriter
                .nonLockingRedisCacheWriter(redisConnectionFactory);

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .prefixCacheNameWith("authentication:")
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        Map<String, RedisCacheConfiguration> perCacheConfig = Map.of(
                "role", defaultConfig.entryTtl(Duration.ofMinutes(60)),
                "user", defaultConfig.entryTtl(Duration.ofMinutes(60)),
                "token", defaultConfig.entryTtl(Duration.ofMinutes(60))
        );

        return RedisCacheManager.builder(redisCacheWriter)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(perCacheConfig)
                .build();
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("10.0.0.222", 6379);
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public CommandLineRunner checkContext(ApplicationContext ctx) {
        return args -> {
            System.out.println("Beans:");
            Arrays.stream(ctx.getBeanDefinitionNames())
                    .filter(name -> name.contains("dummy") || name.contains("controller"))
                    .sorted()
                    .forEach(System.out::println);
        };
    }

}
