package com.guhecloud.cube.base.flow.form.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bq
 * @date 2022/09/08
 * @description 发布表单定义命令
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublishFormDefinitionCommand {

    /**
     * 表单定义key
     */
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

}
