package com.guhecloud.cube.biz.meattrade.server.base.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 理货机运行日志
 *
 * @author lzy
 * @date 2021/9/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TallyOperationLog extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 操作
     */
    private String operation;

    /**
     * 操作时间
     */
    private LocalDateTime time;

}
