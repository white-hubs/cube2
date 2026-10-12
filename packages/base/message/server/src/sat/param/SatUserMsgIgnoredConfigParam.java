package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Set;

@Data
public class SatUserMsgIgnoredConfigParam {

    /**
     * 是否启用
     */
    @NotNull(message = "启用状态不能为空")
    private Boolean enabled;

    /**
     * 开始时间
     */
    private LocalTime startTime;

    /**
     * 结束时间
     */
    private LocalTime endTime;

    /**
     * 是否为次日
     */
    private Boolean tomorrows;

    /**
     * 忽略的通道
     */
    private Set<ChannelEnum> channels;
}
