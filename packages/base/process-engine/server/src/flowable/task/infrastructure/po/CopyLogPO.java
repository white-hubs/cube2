package com.guhecloud.cube.base.flow.flowable.task.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/10/25 19:11
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@TableName("copy_log")
public class CopyLogPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private String taskId;
    private String processInstanceId;
    private String userIds;
    private String data;

    public CopyLogPO() {
        this.id = IdWorker.getId();
    }
}
