package com.aha.aha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;


@Configuration
@EnableRedisRepositories(basePackages = "com.aha.aha.repository")
public class RedisConfig {

    

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
    
        // Restrict deserialization to only your own model packages + java.util
        BasicPolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType("com.aha.aha.entity")   // your entity package
                .allowIfSubType("java.util")            // allow List, Map etc.
                .allowIfSubType("java.lang")            // allow String, Integer etc.
                .build();
    
        GenericJacksonJsonRedisSerializer jsonSerializer = GenericJacksonJsonRedisSerializer
                .builder()
                .typePropertyName("_type")
                .enableDefaultTyping(typeValidator)     // pass the validator here
                .build();
    
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
    
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);
    
        template.afterPropertiesSet();
        return template;
    }
}