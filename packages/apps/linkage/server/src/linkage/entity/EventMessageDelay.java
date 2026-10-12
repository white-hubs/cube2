package com.guhecloud.cube.biz.linkage.server.linkage.entity;

import com.guhecloud.apps.linkage.metadata.enumeration.MessageNotifyTypeEnum;
import com.guhecloud.cube.biz.linkage.server.rule.base.enumeration.DateUnitEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 时间消息延迟升级
 *
 * @author lanchun
 * @date 2022/6/27 9:37
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventMessageDelay extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 通知时间类型
     */
    private MessageNotifyTypeEnum notifyType;

    /**
     * 延迟时间
     */
    private Integer delayTime;

    /**
     * 延迟时间单位
     */
    private DateUnitEnum delayTimeUnit;

    /**
     * 通知时间
     */
    private LocalDateTime notifyTime;

    /**
     * 通知配置
     */
    private String notifyConfig;

    /**
     * 是否发送
     */
    private Boolean notified;
}
