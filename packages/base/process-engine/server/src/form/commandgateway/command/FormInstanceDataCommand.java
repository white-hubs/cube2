package com.guhecloud.cube.base.flow.form.commandgateway.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author bq
 * @date 2022/09/22
 * @description 表单实例数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormInstanceDataCommand {

    private Long id;

    /**
     * 表单实例ID
     */
    private Long formInstanceId;

    /**
     * 表单数据
     */
    private String formData;

}