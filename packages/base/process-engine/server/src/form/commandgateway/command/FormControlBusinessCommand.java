package com.guhecloud.cube.base.flow.form.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * @author bq
 * @date 2022/10/25
 * @description 表单控件与业务字段关联映射
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormControlBusinessCommand {

    private Long id;

    /**
     * 表单定义ID
     */
    @NotNull(message = "没有表单定义")
    private Long formDefinitionId;

    /**
     * 控件ID
     */
    private String controlId;
    /**
     * 控件类型
     */
    private String controlType;
    /**
     * 关联业务字段
     */
    private String businessField;
    /**
     * 组合容器字典绑定
     */
    private List<FormControlBusinessCommand> children;

}