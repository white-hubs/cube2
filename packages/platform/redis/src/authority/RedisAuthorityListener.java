package com.guhecloud.platform.redis.authority;

import com.guhecloud.platform.redis.annotation.RedisSubscribe;
import com.guhecloud.platform.redis.authority.message.AuthorityMessage;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisAuthorityListener {

    private final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer;

    private void synchronize(List<AuthorityMessage> authorityMessages) {
        for (AuthorityMessage authorityMessage : authorityMessages) {
            if (!CollectionUtils.isEmpty(authorityMessage.getResources())) {
                SecurityContextHolder.setAuthorities(authorityMessage.getRoleId().toString(), authorityMessage.getResources());
            } else {
                SecurityContextHolder.removeAuthorities(authorityMessage.getRoleId().toString());
            }
        }
    }

    @RedisSubscribe(topic = "refreshAuthority")
    public void refreshAuthorityConfiguration(String message) {
        try {
            if (!StringUtils.isEmpty(message)) {
                this.synchronize((List<AuthorityMessage>) jackson2JsonRedisSerializer.deserialize(message.getBytes()));
            }
        } catch (Exception e) {
            log.error("Redis authority configuration failed: {}", e.getMessage());
        }
    }
}
