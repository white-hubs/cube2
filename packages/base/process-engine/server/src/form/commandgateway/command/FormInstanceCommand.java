package com.guhecloud.cube.base.flow.form.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单实例命令
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormInstanceCommand {

    private Long id;

    /**
     * 表单定义key
     */
    private String formKey;

    /**
     * 关联业务类型
     */
    private String businessType;

    /**
     * 关联业务key
     */
    private String businessKey;

    /**
     * 表单定义版本
     */
    private Integer version;

}
