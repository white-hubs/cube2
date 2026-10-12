package com.guhecloud.cube.base.message2.server.sat.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class SatMsgTmpQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 场景编号
     */
    @Size(max = 100, message = "场景编号长度不能大于100")
    private String sceneCode;

    /**
     * 通道类型
     */
    @NotNull(message = "通道类型不能为空")
    private ChannelEnum channelCode;

    /**
     * 应用编号
     */
    @Size(max = 100, message = "应用编号不能大于100")
    private String applicationCode;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
