package com.guhecloud.cube.base.flow.form.interfaces.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单实例的数据
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FormInstanceDataVO {

    private Long id;

    /**
     * 表单实例Id
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