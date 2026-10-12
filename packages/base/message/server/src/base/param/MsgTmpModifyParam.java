package com.guhecloud.cube.base.message2.server.base.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 消息模板保存参数
 *
 * @author lanchun
 * @date 2021/11/10 14:22
 */
@Data
public class MsgTmpModifyParam {

    /**
     * 模板名称
     */
    @NotEmpty(message = "模板名称不能为空")
    @Size(min = 2, max = 30, message = "模板名称必须在2到30个字符之间")
    private String name;

    /**
     * 模板内容
     */
    @NotEmpty(message = "模板内容不能为空")
    private String content;

    /**
     * 通道编号
     */
    @NotNull(message = "通道不能为空")
    private ChannelEnum channelCode;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 消息场景编号
     */
    @NotEmpty(message = "消息场景不能为空")
    private String msgSceneCode;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用不能为空")
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;
}
