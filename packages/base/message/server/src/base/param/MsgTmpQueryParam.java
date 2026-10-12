package com.guhecloud.cube.base.message2.server.base.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

@Data
public class MsgTmpQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 应用编号
     */
    private String applicationCode;
}
