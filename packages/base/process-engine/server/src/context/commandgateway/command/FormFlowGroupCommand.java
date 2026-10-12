package com.guhecloud.cube.base.flow.context.commandgateway.command;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 10:18
 * @Description:
 */
@Data
public class FormFlowGroupCommand {

    /**
     * 表单ID
     */
    private Long id;

    /**
     * 名称
     */
    @Length(max = 20, message = "名称限制在20字内")
    @NotEmpty(message = "名称不能为空")
    private String name;

    /**
     * 上级分组ID
     */
    private Long parentId;

    /**
     * 应用
     */
    private String application;
}
