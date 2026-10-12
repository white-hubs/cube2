package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.Data;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 监测规则通知配置
 *
 * @author lanchun
 * @date 2024/9/14 13:52
 */
@TableName("medical_rule_notify_config")
@Data
public class MedicalRuleNotifyConfig extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 超限时间配置
     */
    private Integer times;

    /**
     * 超限时间单位
     */
    private TimeUnit timeUnit;

    /**
     * 通知通道
     */
    private String notifyChannels;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MedicalRuleNotifyConfig that = (MedicalRuleNotifyConfig) o;
        return Objects.equals(getTimes(), that.getTimes()) && getTimeUnit() == that.getTimeUnit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTimes(), getTimeUnit());
    }
}
