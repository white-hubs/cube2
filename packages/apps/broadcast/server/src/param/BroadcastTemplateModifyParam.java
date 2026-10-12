package com.guhecloud.cube.biz.subsystem.broadcast.server.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 广播模板新增
 *
 * @author lanchun
 * @date 2022/12/12 15:07
 */
@Data
public class BroadcastTemplateModifyParam {

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 6, message = "模板名称不能大于6个字符")
    private String name;

    /**
     * 模板内容
     */
    @NotBlank(message = "模板内容不能为空")
    @Size(max = 1000, message = "模板内容不能大于1000个字符")
    private String content;
}
