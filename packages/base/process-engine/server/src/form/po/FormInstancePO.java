package com.guhecloud.cube.base.flow.form.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单实例
 */
@Data
@TableName("form_instance")
@EqualsAndHashCode(callSuper = false)
public class FormInstancePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 表单ID
     */
    private Long formFlowId;

    /**
     * 表单定义key
     */
    private String formKey;

    /**
     * 表单版本号
     */
    private int version;

    /**
     * 关联业务类型
     */
    private String businessType;

    /**
     * 关联业务key
     */
    private String businessKey;

    /**
     * 关联业务状态
     */
    private String businessStatus;

    /**
     * 暂缺少关于业务相关的字段信息
     */

}