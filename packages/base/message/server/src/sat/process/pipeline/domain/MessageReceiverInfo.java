package com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息接收者信息
 *
 * @author lanchun
 * @date 2023/5/15 17:41
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageReceiverInfo {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 接受者标识(根据通道获取,可能是手机号，openId等)
     */
    private String receiver;
}
