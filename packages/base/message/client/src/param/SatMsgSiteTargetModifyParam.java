package com.guhecloud.cube.base.message2.client.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 站内信发送对象
 *
 * @author lanchun
 * @date 2022/6/7 9:56
 */
@Data
public class SatMsgSiteTargetModifyParam {

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空")
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 发送的访问端
     */
    @NotEmpty(message = "发送的访问端不能为空")
    @Size(max = 50, message = "访问端不能大于50")
    private String terminal;
}
