package com.guhecloud.cube.base.message2.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 消息通道查询参数
 *
 * @author lanchun
 * @date 2021/11/10 10:20
 */
@Data
public class MsgChannelQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100个字符")
    private String keyword;
}
