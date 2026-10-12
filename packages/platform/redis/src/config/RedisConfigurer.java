package com.guhecloud.platform.redis.config;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.guhecloud.platform.base.util.DigestUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.client.jackson2.OAuth2ClientJackson2Module;

import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
@ConditionalOnClass(RedisTemplate.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisConfigurer implements CachingConfigurer {

    private final TaskExecutor executor;
    private final LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder builder = new StringBuilder();
            builder.append(target.getClass().getName());
            builder.append(method.getName());
            builder.append("&");
            for (Object param : params) {
                if (param != null) {
                    builder.append(param.getClass().getName());
                    builder.append("&");
                    builder.append(JSON.toJSONString(param));
                    builder.append("&");
                }
            }
            return DigestUtils.sha256Hex(builder.toString());
        };
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(this.lettuceConnectionFactory);
        RedisSerializationContext.SerializationPair<?> serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(this.jackson2JsonRedisSerializer());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)).serializeValuesWith(serializationPair);
        return RedisCacheManager.builder(redisCacheWriter).cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean
    @Primary
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisProperties properties) {
        RedisProperties.Listener config = properties.getListener();
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setTaskExecutor(this.executor);
        container.setConnectionFactory(this.lettuceConnectionFactory);
        container.setRecoveryInterval(config.getRecoveryInterval());
        container.setMaxSubscriptionRegistrationWaitingTime(config.getMaxSubscriptionRegistrationWaitingTime());
        return container;
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(this.jackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(this.jackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, byte[]> bytesRedisTemplate() {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(this.lettuceConnectionFactory);
        // 设置key和value的序列化规则
        redisTemplate.setValueSerializer(RedisSerializer.byteArray());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        // objectMapper for redis serialization
        ObjectMapper objectMapper = JsonMapper.builder()
                .configure(MapperFeature.USE_ANNOTATIONS, true)
                .build();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new OAuth2ClientJackson2Module());
        // jackson2JsonRedisSerializer
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }

}
