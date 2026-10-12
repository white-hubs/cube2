package com.guhecloud.cube.base.message2.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 消息通道保存参数
 *
 * @author lanchun
 * @date 2021/11/10 9:40
 */
@Data
public class MsgChannelModifyParam {

    /**
     * 通道名称
     */
    @Size(max = 30, message = "通道名称不能大于30个字符")
    @NotEmpty(message = "通道名称不能为空")
    private String name;

    /**
     * 通道编号
     */
    @Size(max = 30, message = "通道编号不能大于30个字符")
    @NotEmpty(message = "通道编号不能为空")
    private String code;
}
