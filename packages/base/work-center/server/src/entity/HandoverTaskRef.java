package com.guhecloud.cube.biz.work.center.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/29 16:17
 * @Description: 交接记录关联任务
 */

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("handover_task_ref")
public class HandoverTaskRef extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 交接记录ID
     */
    private Long handoverId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务类型
     */
    private String taskType;

}
