package com.guhecloud.cube.base.flow.form.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/09/16
 * @description 表单定义
 */
@Data
@TableName("form_definition")
@EqualsAndHashCode(callSuper = false)
public class FormDefinitionPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 表单定义key
     */
    @TableField("`key`")
    private String key;

    /**
     * jsonSchema定义
     */
    private String jsonSchema;

    /**
     * UI展示的jsonSchema
     */
    private String jsonSchemaUi;

    /**
     * 版本
     */
    private int version;

    /**
     * 冗余-表单名称
     */
    private String name;

    /**
     * 冗余-表单类型
     */
    private String type;

    /**
     * 冗余-表单应用
     */
    private String application;

    /**
     * 冗余-表单备注
     */
    private String remark;
    /**
     * 是否废弃
     */
    private Boolean abandon;

}