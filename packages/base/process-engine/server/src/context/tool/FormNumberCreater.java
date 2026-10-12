package com.guhecloud.cube.base.flow.context.tool;

import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.platform.base.util.DateUtils.PURE_DATE_FORMAT;

@Slf4j
@Component("flowFormNumberCreater")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormNumberCreater {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据业务类型生成单号
     *
     * @param prefix 前缀
     *
     * @return 单号
     */
    public String create(String prefix) {
        if (log.isDebugEnabled()) {
            log.debug("开始生成表单单号,业务类型为:{}", prefix);
        }
        //Redis 获取Key自增次数 每次+1
        String key = getCacheKey(prefix);
        Long increment = redisTemplate.opsForValue().increment(key, 1);
        if ("9999".equals(increment)) {
            throw new ServiceException("递增单号已用完");
        }

        // 设置过期时间 到第二天+随机时间过期
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        // 毫秒
        long millis = Duration.between(now, next).toMillis();
        int random = (int) (Math.random() * 100);
        redisTemplate.expire(key, millis + random, TimeUnit.MILLISECONDS);

        log.info("生成单号：{}", prefix + getDate() + fill(increment));
        //如果不存在key  默认值设置为30000  方便区分与数据库字段不相同
        if (1 == increment) {
            increment = redisTemplate.opsForValue().increment(key, 1);
        }

        return prefix + getDate() + fill(increment);
    }

    /**
     * 不够4位前面补0
     *
     * @param increment 原始递增号
     *
     * @return 补位后的号
     */
    private String fill(Long increment) {
        if (increment < 10) {
            return "000" + increment;
        } else if (increment < 100) {
            return "00" + increment;
        } else if (increment < 1000) {
            return "0" + increment;
        }
        return increment.toString();
    }

    private String getDate() {
        return DateUtils.format(DateUtils.now(), PURE_DATE_FORMAT);
    }

    private String getCacheKey(String businessType) {
        String prefix = "formNumber";
        String date = getDate();

        StringJoiner stringJoiner = new StringJoiner(":");
        stringJoiner
                .add(prefix)
                .add(SessionContextHolder.getRequester().getTenant().toString())
                .add(businessType)
                .add(date);

        return stringJoiner.toString();
    }
}
