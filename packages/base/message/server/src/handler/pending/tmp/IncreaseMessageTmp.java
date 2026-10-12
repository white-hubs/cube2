package com.guhecloud.cube.base.message2.server.handler.pending.tmp;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncreaseMessageTmp {

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
     * 模板规则
     */
    private String tmpRule;

    /**
     * 模板名称
     */
    private String tmpName;

    /**
     * 模板类型(通知类模板,验证类,营销类)
     */
    private String tmpType;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 模板配置
     */
    private String tmpConfig;
}
