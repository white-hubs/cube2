package com.guhecloud.cube.base.flow.flowable.definition.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/26 10:38
 * @description
 */
@Data
@TableName("process_definition")
public class ProcessDefinitionPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 流程定义key
     */
    @TableField("`key`")
    private String key;

    private String formKey;

    public ProcessDefinitionPO() {
        this.id = IdWorker.getId();
    }
}
