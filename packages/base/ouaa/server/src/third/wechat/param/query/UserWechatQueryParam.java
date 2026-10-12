package com.guhecloud.cube.biz.ouaa.server.third.wechat.param.query;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Collection;

@Data
public class UserWechatQueryParam {

    /**
     * 配置id
     */
    @NotNull(message = "微信配置id不能为空")
    private Long configId;

    /**
     * 系统用户主键
     */
    private Collection<Long> userIds;

    /**
     * 租户编号
     */
    private Integer tenant;
}
