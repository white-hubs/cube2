package com.guhecloud.cube.base.message2.server.base.param;

import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
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
public class MsgChannelManufacturerModifyParam {

    /**
     * 厂家编码
     */
    @Size(max = 30, message = "厂家编码不能大于30个字符")
    @NotEmpty(message = "厂家编码不能为空")
    private ManufacturerCodeEnum code;

    /**
     * 账号规则
     */
    private String accountRule;

    /**
     * 账号规则描述
     */
    private String accountRuleRemark;
}
