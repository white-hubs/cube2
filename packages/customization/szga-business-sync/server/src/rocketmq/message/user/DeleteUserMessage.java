package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.user;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/17 18:01
 * @Description:
 */
@Data
public class DeleteUserMessage {

    /**
     * 消息主题
     */
    public static final String TOPIC = "tenant-user-del";

    /**
     *
     */
    private Long id;

    /**
     * 主键
     */
    private String mobile;
}
