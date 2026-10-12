package com.guhecloud.cube.base.message2.server.handler.pending.tmp;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteMessageTmp {

    /**
     * 通道编号
     */
    private ChannelEnum channelCode;

    /**
     * 厂家编号
     */
    private ManufacturerCodeEnum manufacturerCode;

    /**
     * 通道厂家账号信息
     */
    private String accountInfo;

    /**
     * 模板配置
     */
    private String tmpConfig;
}
