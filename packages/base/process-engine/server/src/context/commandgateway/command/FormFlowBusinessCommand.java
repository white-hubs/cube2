package com.guhecloud.cube.base.flow.context.commandgateway.command;

import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlBusinessCommand;
import lombok.Data;

import java.util.List;

/**
 * @author bq
 * @date 2022/10/18
 * @description 流程表单与业务关联
 */
@Data
public class FormFlowBusinessCommand {

    private Long id;

    /**
     * 流程表单ID
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

    /**
     * 表单定义ID
     */
    private Long formDefinitionId;

    //表单字段映射
    private List<FormControlBusinessCommand> fieldMapList;

}
