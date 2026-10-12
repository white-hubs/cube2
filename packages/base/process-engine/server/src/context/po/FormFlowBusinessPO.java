package com.guhecloud.cube.base.flow.context.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author bq
 * @date 2022/10/28
 * @description 表单流程与业务关联
 */
@Data
@TableName("form_flow_business")
public class FormFlowBusinessPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 流程表单
     */
    private Long formFlowId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

}