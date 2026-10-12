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
public class MsgSceneModifyParam {

    /**
     * 场景编号
     */
    @Size(max = 30, message = "场景编号不能大于30个字符")
    @NotEmpty(message = "场景编号不能为空")
    private String code;

    /**
     * 场景名称
     */
    @Size(max = 30, message = "场景名称不能大于30个字符")
    @NotEmpty(message = "场景名称不能为空")
    private String name;

    /**
     * 消息类型
     */
    @Size(max = 30, message = "消息类型不能大于30个字符")
    private String msgType;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    @Size(max = 50, message = "应用编号不能大于50个字符")
    private String applicationCode;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空")
    @Size(max = 50, message = "应用名称不能大于50个字符")
    private String applicationName;
}
