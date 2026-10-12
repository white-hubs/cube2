package com.guhecloud.cube.base.message2.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 消息场景保存参数
 *
 * @author lanchun
 * @date 2023/3/7 15:24
 */
@Data
public class MsgSceneUpdateParam {

    /**
     * 场景名称
     */
    @Size(max = 30, message = "场景名称不能大于30个字符")
    @NotEmpty(message = "场景名称不能为空")
    private String name;
}
