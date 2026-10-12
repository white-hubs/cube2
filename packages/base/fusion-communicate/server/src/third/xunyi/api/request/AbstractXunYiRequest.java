package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request;

import com.guhecloud.cube.biz.fusion.communicate.server.third.base.util.Md5Utils;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.Base64Utils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AbstractXunYiRequest extends AbstractRequest {

    @Override
    public String getToken() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String key = "XY:DISPATCH:" + tenant;
        RedisTemplate<String, Object> redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        String token = (String) redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(token)) {
            token = getAuthorization();
            redisTemplate.opsForValue().set(key, token, 5, TimeUnit.MINUTES);
        }
        return token;
    }

    /**
     * 获取接口 Authorization，有效期 5 分钟 Authorization：(随机串:时间戳:token)
     *
     * @return
     *
     * @throws Exception
     */
    private String getAuthorization() {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = UUID.randomUUID().toString();
        String token = timestamp + "\n" + nonceStr + "\n" + super.getRequestConfig().getApiKey() + "\n";
        token = Md5Utils.encryptMD5("==" + Md5Utils.encryptMD5(token) + "==");
        token = Base64Utils.encode(token);
        return nonceStr + ":" + timestamp + ":" + token;
    }
}
