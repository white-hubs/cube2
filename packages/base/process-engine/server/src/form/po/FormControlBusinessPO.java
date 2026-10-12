package com.guhecloud.cube.base.flow.form.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/10/19
 * @description 表单控件与业务字段关联
 * 有两种形式存在的关联：
 * 1、系统默认：指定业务控件与指定业务字段关联，无需在页面配置，全局一份无表单定义ID
 * 2、页面手动配置：在表单定义层面指定与业务字段关联，一个控件在不同的定义中有不同的配置，有表单定义ID
 * 优先级：手动配置 > 系统默认
 */
@Data
@TableName("form_control_business")
@EqualsAndHashCode(callSuper = false)
public class FormControlBusinessPO {

    private Long id;

    /**
     * 表单定义ID
     */
    private Long formDefinitionId;

    /**
     * 控件ID
     */
    private String controlId;

    /**
     * 控件类型（基础、人事、考勤）
     */
    private String controlType;

    /**
     * 关联业务类型
     */
    private String businessType;

    /**
     * 关联业务字段
     */
    private String businessField;

}