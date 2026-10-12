package com.guhecloud.cube.base.flow.context.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 10:10
 * 表单流程分组
 */
@Data
@TableName("form_flow_group")
@EqualsAndHashCode(callSuper = false)
public class FormFlowGroupPO extends BasicEnhancedTenantTreeEntity<Long> {

    private Long id;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 应用
     */
    private String application;
}
