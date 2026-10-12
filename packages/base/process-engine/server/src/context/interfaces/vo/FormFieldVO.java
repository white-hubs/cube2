package com.guhecloud.cube.base.flow.context.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bq
 * @date 2022/10/28
 * @description 表单字段
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldVO {

    private String id;

    private String name;

    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

}