package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * @author pengjian
 * @date 2022/10/12 15:55
 * @description
 */
@Data
@AllArgsConstructor
public class PasswordSetting extends BasicEnhancedEntity<Long> {
    private Long id;
    private Integer usefulLife;
    private Integer dueToPromptDay;
    private Boolean forced;
    /**
     * 登录失败次数过多校验是否开启
     */
    private Boolean manyLoginFailuresCalibrated;

    /**
     * 账号登录失败次数过多的锁定时长(目前默认为分钟)
     */
    private Integer accountLoginLockingDuration;

    /**
     * 账号登录失败在一定范围内允许重试的次数
     */
    private Integer accountLoginAllowedFailNum;

    /**
     * 账号登录登录失败允许重试的规定时间范围(目前默认为分钟)
     * (在一定时间内且不超过登录次数的限制下还可继续登录,即超过时间未登录再次重新登录失败时重新计数)
     * (暂时不用,留着后续扩充)
     */
    private Integer accountLoginAllowedFailDuration;

    public PasswordSetting() {
        this.id = IdWorker.getId();
    }

    public PasswordSetting(Integer usefulLife, Integer dueToPromptDay, Boolean forced) {
        this();
        this.dueToPromptDay = dueToPromptDay;
        this.usefulLife = usefulLife;
        this.forced = forced;
    }

    public PasswordSetting(Integer usefulLife, Integer dueToPromptDay, Boolean forced, Boolean manyLoginFailuresCalibrated,
                           Integer accountLoginLockingDuration, Integer accountLoginAllowedFailNum, Integer accountLoginAllowedFailDuration
                          ) {
        this();
        this.usefulLife = usefulLife;
        this.dueToPromptDay = dueToPromptDay;
        this.forced = forced;
        this.manyLoginFailuresCalibrated = manyLoginFailuresCalibrated;
        this.accountLoginLockingDuration = accountLoginLockingDuration;
        this.accountLoginAllowedFailNum = accountLoginAllowedFailNum;
        this.accountLoginAllowedFailDuration = accountLoginAllowedFailDuration;
    }

    public Boolean isValid(LocalDateTime createTime) {
        if (Objects.nonNull(usefulLife)) {
            LocalDateTime usefulLifeDateTime = DateUtils.offsetDay(createTime, usefulLife);
            return DateUtils.between(LocalDateTime.now(), usefulLifeDateTime, ChronoUnit.SECONDS) <= 0;
        }
        return null;
    }

    public LocalDateTime getExpireTime(LocalDateTime createTime) {
        return DateUtils.offsetDay(createTime, usefulLife);
    }

    public Boolean isReminded(LocalDateTime createTime) {
        //过期时间
        LocalDateTime usefulLifeDateTime = DateUtils.offsetDay(createTime, usefulLife);
        long diff = DateUtils.between(LocalDateTime.now(), usefulLifeDateTime, ChronoUnit.DAYS, false);
        return diff <= dueToPromptDay;
    }
}
