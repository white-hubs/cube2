package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 租户消息模板保存参数
 *
 * @author lanchun
 * @date 2023/3/8 17:14
 */
@Data
public class SatMsgTmpModifyParam {

    /**
     * 模板名称
     */
    @NotNull(message = "模板名称不能为空")
    @Size(max = 50, message = "模板名称长度不能大于50")
    private String name;

    /**
     * 模板内容
     */
    @NotNull(message = "模板名称不能为空")
    @Size(max = 500, message = "模板名称长度不能大于500")
    private String content;

    /**
     * 通道编号
     */
    @NotNull(message = "通道编号不能为空")
    private ChannelEnum channelCode;

    /**
     * 是否启用
     */
    @NotNull(message = "是否启用不能为空")
    private Boolean enabled;

    /**
     * 业务类型
     */
    @NotNull(message = "业务类型不能为空")
    @Size(max = 50, message = "业务类型长度不能大于50")
    private String businessType;

    /**
     * 场景编码
     */
    @NotNull(message = "场景编码不能为空")
    @Size(max = 50, message = "场景编码长度不能大于50")
    private String msgSceneCode;

    /**
     * 应用编号
     */
    @NotNull(message = "应用编号不能为空")
    @Size(max = 50, message = "应用编号长度不能大于50")
    private String applicationCode;

    /**
     * 应用名称
     */
    @NotNull(message = "应用名称不能为空")
    @Size(max = 50, message = "应用名称长度不能大于50")
    private String applicationName;

    /**
     * 厂家模板配置
     */
    private String manufacturerTmpConfig;
}
