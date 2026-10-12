package com.guhecloud.cube.base.message2.server.base.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * 模板复制参数
 *
 * @author lanchun
 * @date 2024/11/22 9:47
 */
@Data
public class MsgTmpCopyParam {

    /**
     * 从那个通道复制
     */
    @NotNull(message = "请选择复制的通道")
    private ChannelEnum fromChannel;

    /**
     * 复制到那个通道去
     */
    @NotNull(message = "请选择复制到那个通道去")
    private ChannelEnum toChannel;
}
