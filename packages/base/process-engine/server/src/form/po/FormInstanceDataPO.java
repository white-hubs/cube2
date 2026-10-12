package com.guhecloud.cube.base.flow.form.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单实例的数据
 */
@Data
@TableName("form_instance_data")
@EqualsAndHashCode(callSuper = false)
public class FormInstanceDataPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 表单实例ID
     */
    private Long formInstanceId;

    /**
     * 表单数据
     */
    private String formData;

    /**
     * 表单映射后的业务数据
     */
    private String businessData;

}