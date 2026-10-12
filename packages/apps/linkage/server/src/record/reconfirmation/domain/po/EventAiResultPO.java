package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 事件ai分析结果
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("event_ai_result")
public class EventAiResultPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件记录主键
     */
    private Long eventRecordId;

    /**
     * 原始分析结果
     */
    private String originResult;

    /**
     * 分析结果(确认报警、疑似报警)
     */
    private String result;
}
