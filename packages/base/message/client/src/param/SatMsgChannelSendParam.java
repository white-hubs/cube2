package com.guhecloud.cube.base.message2.client.param;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 发送的通道信息
 *
 * @author lanchun
 * @date 2021/12/22 9:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SatMsgChannelSendParam {

    /**
     * 模板主键(存在多个模板的时候,可指定使用哪一个模板,否则默认取第一个)
     */
    private Long tmpId;

    /**
     * 通道类型
     */
    private ChannelEnum channelCode;

    /**
     * 租户编号
     */
    private Integer tenant;

    /**
     * 通道扩展配置
     */
    private String extendConfig;

    /**
     * 发送对象
     */
    @NotNull(message = "发送对象不能为空")
    @Size(min = 1, message = "发送对象不能为空")
    private List<MsgSendTargetParam> sendTargetParams;

    public SatMsgChannelSendParam(Long tmpId, ChannelEnum channelCode, Integer tenant, List<MsgSendTargetParam> sendTargetParams) {
        this.tmpId = tmpId;
        this.channelCode = channelCode;
        this.tenant = tenant;
        this.sendTargetParams = sendTargetParams;
    }
}
